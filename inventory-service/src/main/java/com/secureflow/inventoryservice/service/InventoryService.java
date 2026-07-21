package com.secureflow.inventoryservice.service;

import com.secureflow.inventoryservice.dto.InventoryResponse;
import com.secureflow.inventoryservice.entity.Inventory;
import com.secureflow.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryResponse checkInventory(Long productId) {

        return inventoryRepository.findByProductId(productId)
                .map(inventory -> new InventoryResponse(
                        productId,
                        inventory.getQuantity() > 0,
                        inventory.getQuantity()
                ))
                .orElse(
                        new InventoryResponse(
                                productId,
                                false,
                                0
                        )
                );
    }

    @Transactional
    public InventoryResponse deductInventory(
            Long productId,
            int requestedQuantity) {

        Inventory inventory = inventoryRepository
                .findByProductId(productId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Inventory not found for product: " + productId
                        )
                );

        if (requestedQuantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero"
            );
        }

        if (inventory.getQuantity() < requestedQuantity) {
            throw new IllegalStateException(
                    "Insufficient inventory for product: " + productId
            );
        }

        inventory.setQuantity(
                inventory.getQuantity() - requestedQuantity
        );

        Inventory saved = inventoryRepository.save(inventory);

        return new InventoryResponse(
                saved.getProductId(),
                saved.getQuantity() > 0,
                saved.getQuantity()
        );
    }

    @Transactional
    public InventoryResponse restoreInventory(Long productId, Integer quantity) {

        Inventory inventory = inventoryRepository
                .findByProductId(productId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Inventory not found for product: " + productId));

        inventory.setQuantity(inventory.getQuantity() + quantity);

        Inventory savedInventory = inventoryRepository.save(inventory);

        return new InventoryResponse(
                savedInventory.getProductId(),
                savedInventory.getQuantity() > 0,
                savedInventory.getQuantity()
        );
    }
}
