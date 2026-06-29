package com.ecommerce.service.impl;

import com.ecommerce.dto.CategoryDto;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.entity.Category;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ConflictException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Page<ProductDto.ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findByActiveTrue(pageable)
                .map(this::toResponse);
    }

    @Override
    public Page<ProductDto.ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryIdAndActiveTrue(categoryId, pageable)
                .map(this::toResponse);
    }

    @Override
    public Page<ProductDto.ProductResponse> searchProducts(String keyword, Pageable pageable) {
        return productRepository.searchByKeyword(keyword, pageable)
                .map(this::toResponse);
    }

    @Override
    public Page<ProductDto.ProductResponse> getProductsByPriceRange(BigDecimal min, BigDecimal max, Pageable pageable) {
        return productRepository.findByPriceRange(min, max, pageable)
                .map(this::toResponse);
    }

    @Override
    public ProductDto.ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
        return toResponse(product);
    }

    @Override
    @Transactional
    public ProductDto.ProductResponse createProduct(ProductDto.ProductRequest request) {
        if (request.getSku() != null && productRepository.existsBySku(request.getSku())) {
            throw new ConflictException("Ya existe un producto con el SKU: " + request.getSku());
        }

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .imageUrl(request.getImageUrl())
                .sku(request.getSku())
                .build();

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría", request.getCategoryId()));
            product.setCategory(category);
        }

        return toResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDto.ProductResponse updateProduct(Long id, ProductDto.ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));

        if (request.getSku() != null && !request.getSku().equals(product.getSku())
                && productRepository.existsBySku(request.getSku())) {
            throw new ConflictException("Ya existe un producto con el SKU: " + request.getSku());
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        product.setSku(request.getSku());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría", request.getCategoryId()));
            product.setCategory(category);
        }

        return toResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void updateStock(Long id, int quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
        int newStock = product.getStock() + quantity;
        if (newStock < 0) {
            throw new BadRequestException("Stock insuficiente. Stock actual: " + product.getStock());
        }
        product.setStock(newStock);
        productRepository.save(product);
    }

    public ProductDto.ProductResponse toResponse(Product product) {
        CategoryDto.CategoryResponse categoryResponse = null;
        if (product.getCategory() != null) {
            categoryResponse = CategoryDto.CategoryResponse.builder()
                    .id(product.getCategory().getId())
                    .name(product.getCategory().getName())
                    .description(product.getCategory().getDescription())
                    .imageUrl(product.getCategory().getImageUrl())
                    .active(product.getCategory().isActive())
                    .build();
        }

        return ProductDto.ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .imageUrl(product.getImageUrl())
                .sku(product.getSku())
                .category(categoryResponse)
                .active(product.isActive())
                .inStock(product.getStock() > 0)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
