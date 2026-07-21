package com.secureflow.inventoryservice.event;

public record InventoryFailedEvent(
        Long orderId,
        String username,
        String reason
) {}