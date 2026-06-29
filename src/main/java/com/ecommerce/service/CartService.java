package com.ecommerce.service;

import com.ecommerce.dto.CartDto;

public interface CartService {
    CartDto.CartResponse getCart(String userEmail);
    CartDto.CartResponse addToCart(String userEmail, CartDto.AddToCartRequest request);
    CartDto.CartResponse updateCartItem(String userEmail, Long itemId, CartDto.UpdateCartItemRequest request);
    CartDto.CartResponse removeFromCart(String userEmail, Long itemId);
    void clearCart(String userEmail);
}
