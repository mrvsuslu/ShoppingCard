package com.system.shoppingcard.service;

import com.system.shoppingcard.domain.Category;
import com.system.shoppingcard.dto.CategoryDTO;
import com.system.shoppingcard.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public void add(CategoryDTO categoryDTO) {
        Category category = createCategory(categoryDTO);
        Optional<Category> parentCategory = categoryRepository.findById(categoryDTO.getParentCategoryId());
        parentCategory.ifPresent(category::setParentCategory);

        categoryRepository.save(category);
    }

    @Override
    public void update(CategoryDTO categoryDTO, long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No such category id"));

        category.setTitle(categoryDTO.getTitle());
        Optional<Category> parentCategory = categoryRepository.findById(categoryDTO.getParentCategoryId());
        parentCategory.ifPresent(category::setParentCategory);

        categoryRepository.save(category);
    }

    @Override
    public void delete(long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category get(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No such category id"));
    }

    private Category createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setTitle(categoryDTO.getTitle());

        return category;
    }

}
