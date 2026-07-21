package com.secureflow.paymentservice.event;

public record PaymentRefundedEvent(
        Long orderId,
        String username,
        String refundStatus
) {}
