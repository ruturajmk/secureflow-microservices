package com.secureflow.productservice.controller;

import com.secureflow.productservice.exception.ProductNotFoundException;
import com.secureflow.productservice.filter.LoggingMdcFilter;
import com.secureflow.productservice.model.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import com.secureflow.productservice.dto.ProductRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Product API",
        description = "Operations related to Products"
)
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger log =
            LoggerFactory.getLogger(ProductController.class);

    @Operation(summary = "Get all products")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    @Cacheable("products")
    public List<Product> getProducts() {

        log.info("Fetching product list");

        return List.of(
                new Product(1L, "Laptop", 65000.0),
                new Product(2L, "Phone", 25000.0),
                new Product(3L, "Keyboard", 1500.0)
        );
    }

    @Operation(summary = "Get product by ID")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{id}")
    @Cacheable(value = "product", key = "#id")
    public Product getProductById(@PathVariable Long id) {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        log.info("Fetching product {}", id);

        return switch (id.intValue()) {

            case 1 -> new Product(1L, "Laptop", 65000.0);

            case 2 -> new Product(2L, "Phone", 25000.0);

            case 3 -> new Product(3L, "Keyboard", 1500.0);

            default -> throw new ProductNotFoundException(id);
        };
    }

    @Operation(summary = "Create a new product")
    @PostMapping
    public Product createProduct(
            @Valid @RequestBody ProductRequest request) {

        log.info("Creating new product {}", request.getName());

        return new Product(
                100L,
                request.getName(),
                request.getPrice()
        );
    }

    @Operation(summary = "Admin endpoint")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminOnly() {
        return "Product Admin Access";
    }

    @GetMapping("/test")
    public String test() {
        return "TEST OK";
    }
}