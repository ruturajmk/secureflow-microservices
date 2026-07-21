package com.secureflow.orderservice.event;

public class PaymentCompletedEvent {

    private Long orderId;
    private String username;
    private String paymentStatus;

    public PaymentCompletedEvent() {
    }

    public PaymentCompletedEvent(Long orderId, String username, String paymentStatus) {
        this.orderId = orderId;
        this.username = username;
        this.paymentStatus = paymentStatus;
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
}