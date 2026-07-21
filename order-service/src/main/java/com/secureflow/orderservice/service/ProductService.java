package com.secureflow.orderservice.service;

import com.secureflow.orderservice.client.ProductClient;
import com.secureflow.orderservice.dto.Product;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductClient productClient;

    public ProductService(ProductClient productClient) {
        this.productClient = productClient;
    }

    @Bulkhead(
            name = "productService",
            fallbackMethod = "productFallback"
    )
    @RateLimiter(
            name = "productService",
            fallbackMethod = "productFallback"
    )
    @Retry(
            name = "productService"
    )
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

        Product product = new Product();
        product.setId(-1L);
        product.setName("Fallback Product");
        product.setPrice(0.0);

        return product;
    }
}
