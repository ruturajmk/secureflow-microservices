package com.secureflow.paymentservice.event;

public record PaymentCompletedEvent(
        Long orderId,
        String username,
        String paymentStatus
) {}
