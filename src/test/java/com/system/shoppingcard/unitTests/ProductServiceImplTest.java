package com.system.shoppingcard.unitTests;

import com.system.shoppingcard.domain.Category;
import com.system.shoppingcard.domain.Product;
import com.system.shoppingcard.dto.ProductDTO;
import com.system.shoppingcard.repository.ProductRepository;
import com.system.shoppingcard.service.CategoryService;
import com.system.shoppingcard.service.ProductServiceImpl;
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
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    public void testAdd() {

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        Category category = new Category();
        category.setTitle("food");
        Product product = new Product();
        product.setTitle("apple");

        ProductDTO productDTO = new ProductDTO();
        long categoryId = 2L;
        productDTO.setCategoryId(categoryId);
        double price = 3L;
        productDTO.setPrice(price);
        productDTO.setTitle("apple");

        when(categoryService.get(categoryId)).thenReturn(category);
        when(productRepository.findById(any())).thenReturn(Optional.of(product));

        productService.add(productDTO);
        verify(productRepository).save(productCaptor.capture());
    }

    @Test
    public void testUpdate() {

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        Category category = new Category();
        category.setTitle("food");
        Product product = new Product();
        product.setTitle("apple");

        ProductDTO productDTO = new ProductDTO();
        long categoryId = 2L;
        productDTO.setCategoryId(categoryId);
        double price = 3L;
        productDTO.setPrice(price);
        productDTO.setTitle("apple");
        long productId = 1L;

        when(categoryService.get(categoryId)).thenReturn(category);
        when(productRepository.findById(any())).thenReturn(Optional.of(product));

        productService.update(productDTO, productId);
        verify(productRepository).save(productCaptor.capture());
    }

    @Test
    public void delete() {

        Category category = new Category();
        category.setTitle("food");
        Product product = new Product();
        product.setTitle("apple");

        ProductDTO productDTO = new ProductDTO();
        long categoryId = 2L;
        productDTO.setCategoryId(categoryId);
        double price = 3L;
        productDTO.setPrice(price);
        productDTO.setTitle("apple");

        when(categoryService.get(categoryId)).thenReturn(category);
        when(productRepository.findById(any())).thenReturn(Optional.of(product));

        productService.delete(product.getId());
        verify(productRepository).deleteById(product.getId());
    }

    @Test
    public void getAll() {
        productService.getAll();
        verify(productRepository).findAll();
    }

    @Test
    public void get() {
        long expectedId = 1L;
        when(productRepository.findById(expectedId)).thenReturn(Optional.of(new Product()));
        productService.get(expectedId);
        verify(productRepository).findById(Mockito.eq(expectedId));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getShouldThrowAnException() {
        productService.get(555);
    }
}