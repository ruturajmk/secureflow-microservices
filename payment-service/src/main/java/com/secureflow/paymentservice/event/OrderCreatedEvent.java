package com.secureflow.paymentservice.event;

public record OrderCreatedEvent(
        Long orderId,
        String username,
        String productName
) {}
