package com.system.shoppingcard.service;

import com.system.shoppingcard.domain.Category;
import com.system.shoppingcard.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    void add(CategoryDTO categoryDTO);

    void update(CategoryDTO categoryDTO, long id);

    void delete(long id);

    List<Category> getAll();

    Category get(long id);

}
