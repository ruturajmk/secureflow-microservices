package com.secureflow.inventoryservice.controller;

import com.secureflow.inventoryservice.dto.InventoryResponse;
import com.secureflow.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/check/{productId}")
    public InventoryResponse checkInventory(
            @PathVariable Long productId) {

        return inventoryService.checkInventory(productId);
    }

    @PostMapping("/deduct/{productId}")
    public InventoryResponse deductInventory(
            @PathVariable Long productId,
            @RequestParam int quantity) {

        return inventoryService.deductInventory(productId, quantity);
    }

    @PostMapping("/restore/{productId}")
    public InventoryResponse restoreInventory(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {

        return inventoryService.restoreInventory(productId, quantity);
    }
}