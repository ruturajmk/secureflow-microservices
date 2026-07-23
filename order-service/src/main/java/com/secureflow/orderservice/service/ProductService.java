package com.secureflow.orderservice.service;

import com.secureflow.orderservice.client.ProductClient;
import com.secureflow.orderservice.dto.Product;
import com.secureflow.orderservice.exception.ProductServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductClient productClient;

    public ProductService(ProductClient productClient) {
        this.productClient = productClient;
    }

    @CircuitBreaker(
            name = "productService",
            fallbackMethod = "productFallback"
    )
    public Product getProduct(Long id) {

        System.out.println("Calling Product Service...");

        return productClient.getProductById(id);
    }

    public Product productFallback(Long id, Throwable ex) {

        System.out.println("PRODUCT FALLBACK CALLED");
        System.out.println("Exception Class = " + ex.getClass().getName());
        System.out.println("Message = " + ex.getMessage());

        throw new ProductServiceUnavailableException(id, ex);
    }
}