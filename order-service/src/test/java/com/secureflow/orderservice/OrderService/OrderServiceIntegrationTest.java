package com.secureflow.orderservice.OrderService;

import com.secureflow.orderservice.client.InventoryClient;
import com.secureflow.orderservice.repository.OrderRepository;
import com.secureflow.orderservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.secureflow.orderservice.TestContainersConfig;
import com.secureflow.orderservice.OrderService.OrderService;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest(properties = {
        "spring.kafka.bootstrap-servers=localhost:9092"
})
class OrderServiceIntegrationTest extends TestContainersConfig {

    @MockBean
    JwtDecoder jwtDecoder;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryClient inventoryClient;

    @Test
    void contextLoads() {
        assertNotNull(orderService);
        assertNotNull(orderRepository);
        assertNotNull(productService);
        assertNotNull(inventoryClient);
    }
}