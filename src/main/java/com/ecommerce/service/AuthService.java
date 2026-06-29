package com.ecommerce.service;

import com.ecommerce.dto.AuthDto;

public interface AuthService {
    AuthDto.AuthResponse register(AuthDto.RegisterRequest request);
    AuthDto.AuthResponse login(AuthDto.LoginRequest request);
}
