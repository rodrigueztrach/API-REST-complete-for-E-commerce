package com.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CartDto {

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class AddToCartRequest {
        @NotNull(message = "El ID del producto es requerido")
        private Long productId;

        @NotNull
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        private Integer quantity;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class UpdateCartItemRequest {
        @NotNull
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        private Integer quantity;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CartItemResponse {
        private Long id;
        private ProductDto.ProductResponse product;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subtotal;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CartResponse {
        private Long id;
        private Long userId;
        private List<CartItemResponse> items;
        private int totalItems;
        private BigDecimal total;
        private LocalDateTime updatedAt;
    }
}
