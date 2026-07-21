package com.secureflow.orderservice.event;

public class PaymentRefundedEvent {

    private Long orderId;
    private String username;
    private String refundStatus;

    public PaymentRefundedEvent() {
    }

    public PaymentRefundedEvent(Long orderId,
                                String username,
                                String refundStatus) {
        this.orderId = orderId;
        this.username = username;
        this.refundStatus = refundStatus;
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

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    @Override
    public String toString() {
        return "PaymentRefundedEvent{" +
                "orderId=" + orderId +
                ", username='" + username + '\'' +
                ", refundStatus='" + refundStatus + '\'' +
                '}';
    }
}
