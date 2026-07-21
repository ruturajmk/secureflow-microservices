package com.secureflow.orderservice.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(
            Long productId,
            int requestedQuantity,
            int availableQuantity) {

        super(
                "Insufficient stock for product: " + productId +
                        ". Requested: " + requestedQuantity +
                        ", Available: " + availableQuantity
        );
    }
}
