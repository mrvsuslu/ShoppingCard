package com.system.shoppingcard.service;

import java.util.List;

public interface DiscountService<Type, DTO> {

    void add(DTO dto);

    void update(DTO dto, long id);

    void delete(long id);

    List<Type> getAll();

    Type get(long id);

}
