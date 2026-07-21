package com.secureflow.paymentservice.event;

public class PaymentFailedEvent {

    private Long orderId;
    private String username;
    private String paymentStatus;
    private String failureReason;

    public PaymentFailedEvent() {
    }

    public PaymentFailedEvent(
            Long orderId,
            String username,
            String paymentStatus,
            String failureReason) {

        this.orderId = orderId;
        this.username = username;
        this.paymentStatus = paymentStatus;
        this.failureReason = failureReason;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }
}
