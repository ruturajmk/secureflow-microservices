package com.secureflow.notificationservice.event;

public record InventoryReservedEvent(
        Long orderId,
        String username,
        String inventoryStatus
) {}