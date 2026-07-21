package com.secureflow.orderservice.client;

import com.secureflow.orderservice.config.FeignConfig;
import com.secureflow.orderservice.dto.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "product-service",
        configuration = FeignConfig.class
)
public interface ProductClient {

    @GetMapping("/api/products")
    List<Product> getProducts();

    @GetMapping("/api/products/{id}")
    Product getProductById(@PathVariable Long id);
}
