package com.secureflow.orderservice.event;

import com.secureflow.orderservice.client.InventoryClient;
import com.secureflow.orderservice.entity.Order;
import com.secureflow.orderservice.event.PaymentFailedEvent;
import com.secureflow.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentFailedEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(PaymentFailedEventConsumer.class);

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public PaymentFailedEventConsumer(
            OrderRepository orderRepository,
            InventoryClient inventoryClient) {

        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
    }

    @KafkaListener(
            topics = "payment-failed-topic",
            groupId = "order-service-payment-failed-group",
            containerFactory = "paymentFailedKafkaListenerContainerFactory"
    )
    public void consume(PaymentFailedEvent event) {

        log.info(
                "PAYMENT FAILED EVENT RECEIVED => orderId={}, username={}, paymentStatus={}, failureReason={}",
                event.getOrderId(),
                event.getUsername(),
                event.getPaymentStatus(),
                event.getFailureReason()
        );

        Order order = orderRepository
                .findById(event.getOrderId())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Order not found: " + event.getOrderId()
                        )
                );

        // Update failed order status
        order.setStatus("PAYMENT_FAILED");

        orderRepository.save(order);

        log.info(
                "ORDER STATUS UPDATED => orderId={}, status={}",
                order.getId(),
                order.getStatus()
        );

        // Saga compensation:
        // restore the inventory deducted while creating the order
        inventoryClient.restoreInventory(
                order.getProductId(),
                order.getQuantity()
        );

        log.info(
                "INVENTORY RESTORED => orderId={}, productId={}, quantity={}",
                order.getId(),
                order.getProductId(),
                order.getQuantity()
        );
    }
}
