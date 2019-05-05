package com.system.shoppingcard.dto;

import lombok.Data;

@Data
public class ProductResultDTO {

    private String categoryName;
    private String productName;
    private long quantity;
    private double unitPrice;

}
