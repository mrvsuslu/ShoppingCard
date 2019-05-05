package com.system.shoppingcard.dto;

import lombok.Data;

@Data
public class CouponDTO {

    private double minimumPurchaseAmount;
    private double discountRate;
    private String discountType;
    private String name;

}
