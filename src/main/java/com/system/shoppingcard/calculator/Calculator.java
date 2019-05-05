package com.system.shoppingcard.calculator;

import com.system.shoppingcard.domain.Campaign;
import com.system.shoppingcard.domain.Coupon;
import com.system.shoppingcard.domain.ShoppingCard;
import com.system.shoppingcard.dto.DeliveryCostDTO;

import java.util.List;

public interface Calculator {

    ShoppingCard calculateCouponDiscount(ShoppingCard cart, Coupon coupon);

    ShoppingCard calculateCampaignDiscount(ShoppingCard cart, List<Campaign> campaignList);

    double getCouponDiscount(ShoppingCard shoppingCardDTO);

    double getCampaignDiscount(ShoppingCard shoppingCardDTO);

    double getTotalAmountAfterDiscounts(ShoppingCard shoppingCardDTO);

    double getDeliveryCost(ShoppingCard shoppingCardDTO, DeliveryCostDTO deliveryCostDTO, int numberOfDeliveries);

}
