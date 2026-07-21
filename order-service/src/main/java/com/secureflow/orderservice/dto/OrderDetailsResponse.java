package com.secureflow.orderservice.dto;

import java.util.List;

public record OrderDetailsResponse(
        String username,
        List<Product> products
) {
}
