package com.system.shoppingcard.dto;

import lombok.Data;

@Data
public class CampaignDTO {

    private long categoryId;
    private double discount;
    private double purchasedItem;
    private String discountType;

}
