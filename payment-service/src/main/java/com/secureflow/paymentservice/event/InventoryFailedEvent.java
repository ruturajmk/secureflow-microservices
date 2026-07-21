package com.secureflow.paymentservice.event;

public record InventoryFailedEvent(
        Long orderId,
        String username,
        String reason
) {}