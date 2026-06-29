package com.ecommerce.service;

import com.ecommerce.dto.CategoryDto;
import java.util.List;

public interface CategoryService {
    List<CategoryDto.CategoryResponse> getAllCategories();
    CategoryDto.CategoryResponse getCategoryById(Long id);
    CategoryDto.CategoryResponse createCategory(CategoryDto.CategoryRequest request);
    CategoryDto.CategoryResponse updateCategory(Long id, CategoryDto.CategoryRequest request);
    void deleteCategory(Long id);
}
