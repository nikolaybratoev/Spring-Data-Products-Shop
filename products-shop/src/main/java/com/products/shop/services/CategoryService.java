package com.products.shop.services;

import com.products.shop.dtos.CategoryDto;
import com.products.shop.dtos.CategorySeedDto;
import com.products.shop.entities.Category;

import java.util.List;

public interface CategoryService {

    void seedCategories(CategorySeedDto[] categorySeedDtos);

    List<Category> getRandomCategories();

    List<CategoryDto> getAllCategories();
}
