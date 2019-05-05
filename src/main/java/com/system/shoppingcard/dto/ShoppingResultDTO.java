package com.system.shoppingcard.dto;

import lombok.Data;

import java.util.List;

@Data
public class ShoppingResultDTO {

    private List<ProductResultDTO> products;
    private double totalPrice;
    private double totalDiscount;
    private double totalAmount;
    private double deliveryCost;

}
