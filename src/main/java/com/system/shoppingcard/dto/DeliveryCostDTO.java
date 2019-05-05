package com.system.shoppingcard.dto;

import lombok.Data;

@Data
public class DeliveryCostDTO {

    private double costPerDelivery;
    private double costPerProduct;
    private double fixedCost;
    private long shoppingCardId;

}
