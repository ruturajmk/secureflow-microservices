package com.secureflow.orderservice.exception;

public class ProductServiceUnavailableException extends RuntimeException {

    public ProductServiceUnavailableException(
            Long productId,
            Throwable cause) {

        super(
                "Product Service is currently unavailable. " +
                        "Unable to process product: " + productId,
                cause
        );
    }
}
