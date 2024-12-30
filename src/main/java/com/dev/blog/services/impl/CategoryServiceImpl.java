package com.dev.blog.services.impl;

import com.dev.blog.domain.entities.Category;
import com.dev.blog.repositories.CategoryRepository;
import com.dev.blog.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAllWithPostCount();
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        String name = category.getName();

        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException("Category already exists with name: " + name);
        }

        return categoryRepository.save(category);
    }
}
