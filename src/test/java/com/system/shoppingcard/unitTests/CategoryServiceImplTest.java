package com.system.shoppingcard.unitTests;

import com.system.shoppingcard.domain.Category;
import com.system.shoppingcard.dto.CategoryDTO;
import com.system.shoppingcard.repository.CategoryRepository;
import com.system.shoppingcard.service.CategoryServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    public void testAdd() {

        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);

        Category category = new Category();
        category.setTitle("food");

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setTitle("food");

        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));

        categoryService.add(categoryDTO);
        verify(categoryRepository).save(categoryCaptor.capture());
    }

    @Test
    public void testUpdate() {

        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);

        Category category = new Category();
        category.setTitle("food");
        long categoryId = 1L;

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setTitle("drink");

        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));

        categoryService.update(categoryDTO, categoryId);
        verify(categoryRepository).save(categoryCaptor.capture());
    }

    @Test
    public void delete() {

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setTitle("food");

        long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);

        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));

        categoryService.delete(categoryId);
        verify(categoryRepository).deleteById(category.getId());
    }

    @Test
    public void getAll() {
        categoryService.getAll();
        verify(categoryRepository).findAll();
    }

    @Test
    public void get() {
        long expectedId = 1L;
        when(categoryRepository.findById(expectedId)).thenReturn(Optional.of(new Category()));
        categoryService.get(expectedId);
        verify(categoryRepository).findById(Mockito.eq(expectedId));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getShouldThrowAnException() {
        categoryService.get(555);
    }
}