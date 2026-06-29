package com.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class CategoryDto {

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class CategoryRequest {
        @NotBlank(message = "El nombre de la categoría es requerido")
        private String name;
        private String description;
        private String imageUrl;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CategoryResponse {
        private Long id;
        private String name;
        private String description;
        private String imageUrl;
        private boolean active;
        private Integer productCount;
    }
}
