package com.secureflow.orderservice.OrderService;

import com.secureflow.orderservice.client.InventoryClient;
import com.secureflow.orderservice.dto.CreateOrderRequest;
import com.secureflow.orderservice.dto.InventoryResponse;
import com.secureflow.orderservice.dto.OrderResponse;
import com.secureflow.orderservice.dto.Product;
import com.secureflow.orderservice.entity.Order;
import com.secureflow.orderservice.kafka.OrderEventProducer;
import com.secureflow.orderservice.repository.OrderRepository;
import com.secureflow.orderservice.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository repository;

    @Mock
    private ProductService productService;

    @Mock
    private InventoryClient inventoryClient;

    @Mock
    private OrderEventProducer orderEventProducer;

    @InjectMocks
    private OrderService orderService;

    private Jwt jwt;

    @BeforeEach
    void setup() {

        jwt = mock(Jwt.class);

        when(jwt.getSubject())
                .thenReturn("alice");
    }

    @Test
    void createOrder_ShouldCreateOrderSuccessfully() {

        CreateOrderRequest request =
                new CreateOrderRequest();

        request.setProductId(1L);
        request.setQuantity(2);

        Product product =
                new Product();

        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(1000.0);

        InventoryResponse inventory =
                new InventoryResponse();

        inventory.setProductId(1L);
        inventory.setAvailable(true);
        inventory.setQuantity(10);

        when(productService.getProduct(1L))
                .thenReturn(product);

        when(inventoryClient.checkInventory(1L))
                .thenReturn(inventory);

        Order savedOrder =
                new Order();

        savedOrder.setId(100L);
        savedOrder.setProductId(1L);
        savedOrder.setQuantity(2);
        savedOrder.setUsername("alice");
        savedOrder.setStatus("CREATED");

        when(repository.save(any(Order.class)))
                .thenReturn(savedOrder);

        OrderResponse response =
                orderService.createOrder(request, jwt);

        assertNotNull(response);
        assertEquals(100L, response.getOrderId());
        assertEquals("CREATED", response.getStatus());
        assertTrue(response.getMessage().contains("Laptop"));

        verify(productService).getProduct(1L);

        verify(inventoryClient).checkInventory(1L);

        verify(inventoryClient)
                .deductInventory(1L, 2);

        verify(repository)
                .save(any(Order.class));

        verify(orderEventProducer)
                .publish(any());

        ArgumentCaptor<Order> captor =
                ArgumentCaptor.forClass(Order.class);

        verify(repository).save(captor.capture());

        Order captured =
                captor.getValue();

        assertEquals(1L, captured.getProductId());
        assertEquals(2, captured.getQuantity());
        assertEquals("alice", captured.getUsername());
        assertEquals("CREATED", captured.getStatus());
    }
}
