package com.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductDto {

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ProductRequest {
        @NotBlank(message = "El nombre del producto es requerido")
        private String name;

        private String description;

        @NotNull(message = "El precio es requerido")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
        private BigDecimal price;

        @NotNull(message = "El stock es requerido")
        @Min(value = 0, message = "El stock no puede ser negativo")
        private Integer stock;

        private String imageUrl;
        private String sku;
        private Long categoryId;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ProductResponse {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private Integer stock;
        private String imageUrl;
        private String sku;
        private CategoryDto.CategoryResponse category;
        private boolean active;
        private boolean inStock;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
