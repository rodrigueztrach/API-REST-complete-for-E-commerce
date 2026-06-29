package com.ecommerce.service;

import com.ecommerce.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ProductService {
    Page<ProductDto.ProductResponse> getAllProducts(Pageable pageable);
    Page<ProductDto.ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable);
    Page<ProductDto.ProductResponse> searchProducts(String keyword, Pageable pageable);
    Page<ProductDto.ProductResponse> getProductsByPriceRange(BigDecimal min, BigDecimal max, Pageable pageable);
    ProductDto.ProductResponse getProductById(Long id);
    ProductDto.ProductResponse createProduct(ProductDto.ProductRequest request);
    ProductDto.ProductResponse updateProduct(Long id, ProductDto.ProductRequest request);
    void deleteProduct(Long id);
    void updateStock(Long id, int quantity);
}
