package com.ecommerce.dto;

import com.ecommerce.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

public class AuthDto {

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class RegisterRequest {
        @NotBlank(message = "El nombre es requerido")
        private String firstName;

        @NotBlank(message = "El apellido es requerido")
        private String lastName;

        @Email(message = "Formato de email inválido")
        @NotBlank(message = "El email es requerido")
        private String email;

        @NotBlank(message = "La contraseña es requerida")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        private String password;

        private String phone;
        private String address;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class LoginRequest {
        @Email @NotBlank(message = "El email es requerido")
        private String email;

        @NotBlank(message = "La contraseña es requerida")
        private String password;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AuthResponse {
        private String token;
        private String type;
        private Long userId;
        private String email;
        private String fullName;
        private User.Role role;
    }
}
