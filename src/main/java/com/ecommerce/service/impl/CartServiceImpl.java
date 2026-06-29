package com.ecommerce.service.impl;

import com.ecommerce.dto.CartDto;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductServiceImpl productServiceImpl;

    @Override
    public CartDto.CartResponse getCart(String userEmail) {
        Cart cart = getCartByEmail(userEmail);
        return toResponse(cart);
    }

    @Override
    @Transactional
    public CartDto.CartResponse addToCart(String userEmail, CartDto.AddToCartRequest request) {
        Cart cart = getCartByEmail(userEmail);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto", request.getProductId()));

        if (!product.isActive()) {
            throw new BadRequestException("El producto no está disponible");
        }
        if (product.getStock() < request.getQuantity()) {
            throw new BadRequestException("Stock insuficiente. Disponible: " + product.getStock());
        }

        // Check if product already in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQty = item.getQuantity() + request.getQuantity();
            if (newQty > product.getStock()) {
                throw new BadRequestException("Stock insuficiente. Disponible: " + product.getStock());
            }
            item.setQuantity(newQty);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .unitPrice(product.getPrice())
                    .build();
            cart.getItems().add(newItem);
        }

        return toResponse(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartDto.CartResponse updateCartItem(String userEmail, Long itemId, CartDto.UpdateCartItemRequest request) {
        Cart cart = getCartByEmail(userEmail);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item del carrito", itemId));

        if (request.getQuantity() > item.getProduct().getStock()) {
            throw new BadRequestException("Stock insuficiente. Disponible: " + item.getProduct().getStock());
        }

        item.setQuantity(request.getQuantity());
        return toResponse(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartDto.CartResponse removeFromCart(String userEmail, Long itemId) {
        Cart cart = getCartByEmail(userEmail);
        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        return toResponse(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public void clearCart(String userEmail) {
        Cart cart = getCartByEmail(userEmail);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    private Cart getCartByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + email));
        return cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado para el usuario"));
    }

    private CartDto.CartResponse toResponse(Cart cart) {
        List<CartDto.CartItemResponse> items = cart.getItems().stream()
                .map(item -> CartDto.CartItemResponse.builder()
                        .id(item.getId())
                        .product(productServiceImpl.toResponse(item.getProduct()))
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .subtotal(item.getSubtotal())
                        .build())
                .toList();

        return CartDto.CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .items(items)
                .totalItems(cart.getTotalItems())
                .total(cart.getTotal())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }
}
