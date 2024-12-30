package com.dev.blog.services;

import com.dev.blog.domain.entities.Category;

import java.util.List;

public interface CategoryService {

    List<Category> listCategories();

    Category createCategory(Category category);
}
