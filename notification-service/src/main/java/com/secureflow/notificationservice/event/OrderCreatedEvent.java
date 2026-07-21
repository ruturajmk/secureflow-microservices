package com.secureflow.notificationservice.event;

public record OrderCreatedEvent(
        Long orderId,
        String username,
        String productName
) {}
