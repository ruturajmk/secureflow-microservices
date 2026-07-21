package com.secureflow.inventoryservice.event;

public record PaymentCompletedEvent(
        Long orderId,
        String username,
        String paymentStatus
) {}
