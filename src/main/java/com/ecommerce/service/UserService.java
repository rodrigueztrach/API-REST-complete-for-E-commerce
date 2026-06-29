package com.ecommerce.service;

import com.ecommerce.dto.UserDto;
import java.util.List;

public interface UserService {
    UserDto.UserResponse getCurrentUser(String email);
    UserDto.UserResponse updateUser(String email, UserDto.UpdateUserRequest request);
    void changePassword(String email, UserDto.ChangePasswordRequest request);
    List<UserDto.UserResponse> getAllUsers();
    UserDto.UserResponse getUserById(Long id);
    void deactivateUser(Long id);
}
