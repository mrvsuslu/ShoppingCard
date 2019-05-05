package com.system.shoppingcard.service;

import com.system.shoppingcard.domain.Product;
import com.system.shoppingcard.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    void add(ProductDTO productDTO);

    void update(ProductDTO productDTO, long id);

    void delete(long id);

    List<Product> getAll();

    Product get(long id);

}
