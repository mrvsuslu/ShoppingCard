package com.system.shoppingcard.service;

import com.system.shoppingcard.domain.Category;
import com.system.shoppingcard.domain.Product;
import com.system.shoppingcard.dto.ProductDTO;
import com.system.shoppingcard.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    private static final String NO_PRODUCT_ID = "No such product id";

    @Override
    public void add(ProductDTO productDTO) {
        Product product = createProduct(productDTO);
        Category category = categoryService.get(productDTO.getCategoryId());
        product.setCategory(category);
        productRepository.save(product);
    }

    @Override
    public void update(ProductDTO productDTO, long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NO_PRODUCT_ID));

        product.setPrice(productDTO.getPrice());
        product.setTitle(productDTO.getTitle());
        Category category = categoryService.get(productDTO.getCategoryId());
        product.setCategory(category);
        productRepository.save(product);
    }

    @Override
    public void delete(long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Product get(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NO_PRODUCT_ID));
    }

    private Product createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setPrice(productDTO.getPrice());
        product.setTitle(productDTO.getTitle());

        return product;
    }
}
