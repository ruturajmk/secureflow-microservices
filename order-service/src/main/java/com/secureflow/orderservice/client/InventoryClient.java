package com.secureflow.orderservice.client;

import com.secureflow.orderservice.config.FeignConfig;
import com.secureflow.orderservice.dto.InventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "inventory-service",
        configuration = FeignConfig.class
)
public interface InventoryClient {

    @GetMapping("/api/inventory/check/{productId}")
    InventoryResponse checkInventory(
            @PathVariable("productId") Long productId);

    @PostMapping("/api/inventory/deduct/{productId}")
    InventoryResponse deductInventory(
            @PathVariable("productId") Long productId,
            @RequestParam("quantity") int quantity);

    @PostMapping("/api/inventory/restore/{productId}")
    void restoreInventory(
            @PathVariable("productId") Long productId,
            @RequestParam("quantity") Integer quantity
    );
}