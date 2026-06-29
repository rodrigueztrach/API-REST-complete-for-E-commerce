package com.ecommerce.dto;

import com.ecommerce.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

public class UserDto {

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UserResponse {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String address;
        private User.Role role;
        private boolean active;
        private LocalDateTime createdAt;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class UpdateUserRequest {
        @Size(min = 2, message = "El nombre debe tener al menos 2 caracteres")
        private String firstName;

        @Size(min = 2, message = "El apellido debe tener al menos 2 caracteres")
        private String lastName;

        private String phone;
        private String address;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ChangePasswordRequest {
        @NotBlank(message = "La contraseña actual es requerida")
        private String currentPassword;

        @NotBlank @Size(min = 6, message = "La nueva contraseña debe tener al menos 6 caracteres")
        private String newPassword;
    }
}
