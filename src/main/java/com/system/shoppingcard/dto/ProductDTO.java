package com.system.shoppingcard.dto;

import lombok.Data;

@Data
public class ProductDTO {

    private String title;
    private double price;
    private long categoryId;

}
