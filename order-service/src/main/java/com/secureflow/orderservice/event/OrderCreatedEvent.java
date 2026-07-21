package com.secureflow.orderservice.event;

public record OrderCreatedEvent(
        Long orderId,
        String username,
        String productName
) {}