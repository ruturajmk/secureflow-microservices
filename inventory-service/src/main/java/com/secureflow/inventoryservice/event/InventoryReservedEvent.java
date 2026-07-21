package com.secureflow.inventoryservice.event;

public record InventoryReservedEvent(
        Long orderId,
        String username,
        String inventoryStatus
) {}
