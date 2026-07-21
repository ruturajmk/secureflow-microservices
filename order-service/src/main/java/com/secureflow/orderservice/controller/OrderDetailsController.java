package com.secureflow.orderservice.controller;

import com.secureflow.orderservice.client.ProductClient;
import com.secureflow.orderservice.dto.OrderDetailsResponse;
import com.secureflow.orderservice.dto.Product;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderDetailsController {

    private final ProductClient productClient;

    public OrderDetailsController(ProductClient productClient) {
        this.productClient = productClient;
    }

    @GetMapping("/api/orders/details")
    public OrderDetailsResponse orderDetails(
            @AuthenticationPrincipal Jwt jwt) {

        try {

            List<Product> products =
                    productClient.getProducts();

            return new OrderDetailsResponse(
                    jwt.getSubject(),
                    products
            );

        } catch (Exception ex) {

            ex.printStackTrace();

            throw ex;
        }
    }

    public OrderDetailsResponse fallbackProducts(
            Jwt jwt,
            Exception ex) {

        ex.printStackTrace();

        System.out.println("FALLBACK EXCEPTION = "
                + ex.getClass().getName());

        System.out.println("FALLBACK MESSAGE = "
                + ex.getMessage());

        return new OrderDetailsResponse(
                jwt.getSubject(),
                List.of(
                        new Product(
                                -1L,
                                ex.getClass().getSimpleName(),
                                0.0
                        )
                )
        );
    }
}