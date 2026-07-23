package com.secureflow.orderservice.OrderService;

import com.secureflow.orderservice.client.InventoryClient;
import com.secureflow.orderservice.service.ProductService;
import com.secureflow.orderservice.dto.CreateOrderRequest;
import com.secureflow.orderservice.dto.InventoryResponse;
import com.secureflow.orderservice.dto.OrderResponse;
import com.secureflow.orderservice.dto.Product;
import com.secureflow.orderservice.entity.Order;
import com.secureflow.orderservice.kafka.OrderEventProducer;
import com.secureflow.orderservice.repository.OrderRepository;
import feign.FeignException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import com.secureflow.orderservice.event.OrderCreatedEvent;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final ProductService productService;
    private final InventoryClient inventoryClient;
    private final OrderEventProducer orderEventProducer;

    public OrderService(
        OrderRepository repository,
        ProductService productService,
        InventoryClient inventoryClient,
        OrderEventProducer orderEventProducer) {

    	this.repository = repository;
    	this.productService = productService;
   	this.inventoryClient = inventoryClient;
    	this.orderEventProducer = orderEventProducer;
    }

    public OrderResponse createOrder(
            CreateOrderRequest request,
            Jwt jwt) {

        /*
         * STEP 1:
         * Validate the request.
         */
        if (request.getProductId() == null) {
            throw new IllegalArgumentException(
                    "Product ID must not be null"
            );
        }
        if (request.getQuantity() == null ||
                request.getQuantity() <= 0) {

            throw new IllegalArgumentException(
                    "Order quantity must be greater than zero"
            );
        }


        /*
         * STEP 2:
         * Validate product by calling Product Service.
         */
        Product product = productService.getProduct(
        request.getProductId());

        /*
         * STEP 3:
         * Check inventory by calling Inventory Service.
         */
        InventoryResponse inventory;

        try {
            inventory = inventoryClient.checkInventory(
                    request.getProductId()
            );
        } catch (FeignException.NotFound ex) {

            throw new IllegalArgumentException(
                    "Inventory not found for product: "
                            + request.getProductId()
            );
        } catch (FeignException ex) {

            throw new RuntimeException(
                    "Unable to communicate with Inventory Service",
                    ex
            );
        }

        /*
         * STEP 4:
         * Check whether inventory is available.
         */
        if (!inventory.isAvailable()) {

            throw new IllegalStateException(
                    "Product is out of stock: "
                            + request.getProductId()
            );
        }

        /*
         * STEP 5:
         * Check whether requested quantity is available.
         */
        if (inventory.getQuantity() < request.getQuantity()) {
            throw new IllegalStateException(
                    "Insufficient inventory for product: "
                            + request.getProductId()
                            + ". Available: "
                            + inventory.getQuantity()
                            + ", Requested: "
                            + request.getQuantity()
            );
        }

        /*
         * STEP 6:
         * Deduct inventory.
         *
         * Inventory Service performs the actual database update.
         */
        try {
            inventoryClient.deductInventory(
                    request.getProductId(),
                    request.getQuantity()
            );
        } catch (FeignException.NotFound ex) {

            throw new IllegalArgumentException(
                    "Inventory not found while deducting stock for product: "
                            + request.getProductId()
            );

        } catch (FeignException.BadRequest ex) {

            throw new IllegalStateException(
                    "Unable to deduct inventory for product: "
                            + request.getProductId()
                            + ". Requested quantity: "
                            + request.getQuantity()
            );

        } catch (FeignException ex) {

            throw new RuntimeException(
                    "Inventory deduction failed for product: "
                            + request.getProductId(),
                    ex
            );
        }

        /*
         * STEP 7:
         * Create the Order entity.
         *
         * This happens only after:
         *
         * 1. Product validation succeeds
         * 2. Inventory check succeeds
         * 3. Inventory quantity validation succeeds
         * 4. Inventory deduction succeeds
         */
        Order order = new Order();

        order.setProductId(
                request.getProductId()
        );
        order.setQuantity(
                request.getQuantity()
        );
        order.setUsername(
                jwt.getSubject()
        );
        order.setStatus(
                "CREATED"
        );
        order.setCreatedAt(
                LocalDateTime.now()
        );

        /*
         * STEP 8:
         * Save order in Order Service database.
         */
        Order saved;

        try {
            saved = repository.save(order);

        } catch (Exception ex) {

            /*
             * IMPORTANT:
             *
             * Inventory has already been deducted at this point.
             *
             * If saving the order fails, the inventory deduction
             * cannot automatically be rolled back because
             * Order Service and Inventory Service have separate
             * databases / transactions.
             *
             * We will solve this later using Saga pattern and
             * compensation events through Kafka.
             */

            throw new RuntimeException(
                    "Inventory was deducted, but order creation failed",
                    ex
            );
        }

        /*
         * STEP 9:
         * Publish OrderCreatedEvent to Kafka.
         *
         * Payment Service listens to order-created-topic
         * and processes the payment asynchronously.
         */
        OrderCreatedEvent orderCreatedEvent =
                new OrderCreatedEvent(
                        saved.getId(),
                        saved.getUsername(),
                        product.getName()
                );

        orderEventProducer.publish(orderCreatedEvent);

        /*
         * STEP 10:
         * Prepare successful response.
         */
        OrderResponse response =
                new OrderResponse();

        response.setOrderId(
                saved.getId()
        );
        response.setStatus(
                saved.getStatus()
        );
        response.setMessage(
                "Order Created Successfully for product: "
                        + product.getName()
        );
        return response;
    }
}