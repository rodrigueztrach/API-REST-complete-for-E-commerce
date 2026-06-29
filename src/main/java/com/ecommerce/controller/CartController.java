package com.ecommerce.controller;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.dto.CartDto;
import com.ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Carrito de Compras", description = "Gestión del carrito de compras")
@SecurityRequirement(name = "bearerAuth")
public class CartController {

    private final CartService cartService;

    @GetMapping
    @Operation(summary = "Obtener el carrito del usuario autenticado")
    public ResponseEntity<ApiResponse<CartDto.CartResponse>> getCart(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(cartService.getCart(userDetails.getUsername())));
    }

    @PostMapping("/items")
    @Operation(summary = "Agregar producto al carrito")
    public ResponseEntity<ApiResponse<CartDto.CartResponse>> addToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CartDto.AddToCartRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Producto agregado al carrito",
                cartService.addToCart(userDetails.getUsername(), request)));
    }

    @PutMapping("/items/{itemId}")
    @Operation(summary = "Actualizar cantidad de un item en el carrito")
    public ResponseEntity<ApiResponse<CartDto.CartResponse>> updateItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long itemId,
            @Valid @RequestBody CartDto.UpdateCartItemRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Carrito actualizado",
                cartService.updateCartItem(userDetails.getUsername(), itemId, request)));
    }

    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "Eliminar un item del carrito")
    public ResponseEntity<ApiResponse<CartDto.CartResponse>> removeItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long itemId) {
        return ResponseEntity.ok(ApiResponse.success("Item eliminado del carrito",
                cartService.removeFromCart(userDetails.getUsername(), itemId)));
    }

    @DeleteMapping
    @Operation(summary = "Vaciar el carrito completo")
    public ResponseEntity<ApiResponse<Void>> clearCart(
            @AuthenticationPrincipal UserDetails userDetails) {
        cartService.clearCart(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Carrito vaciado"));
    }
}
