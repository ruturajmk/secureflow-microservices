package com.secureflow.inventoryservice.dto;

public class InventoryResponse {

    private Long productId;
    private boolean available;
    private int quantity;

    public InventoryResponse() {
    }

    public InventoryResponse(Long productId, boolean available, int quantity) {
        this.productId = productId;
        this.available = available;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
