package com.system.shoppingcard.calculator;

import com.system.shoppingcard.domain.Campaign;
import com.system.shoppingcard.domain.Coupon;
import com.system.shoppingcard.domain.ShoppingCard;
import com.system.shoppingcard.dto.DeliveryCostDTO;

import java.util.List;

public abstract class CalculatorAbst implements Calculator {

    @Override
    public ShoppingCard calculateCouponDiscount(ShoppingCard shoppingCard, Coupon coupon) {
        return applyCouponDiscount(shoppingCard, coupon);
    }

    protected abstract ShoppingCard applyCouponDiscount(ShoppingCard shoppingCard, Coupon coupon);

    @Override
    public ShoppingCard calculateCampaignDiscount(ShoppingCard shoppingCard, List<Campaign> campaignList) {
        return applyCampaignDiscount(shoppingCard, campaignList);
    }

    protected abstract ShoppingCard applyCampaignDiscount(ShoppingCard shoppingCard, List<Campaign> campaignList);

    @Override
    public double getDeliveryCost(ShoppingCard shoppingCard, DeliveryCostDTO deliveryCostDTO, int numberOfDeliveries) {
        return calculateFor(shoppingCard, deliveryCostDTO, numberOfDeliveries);
    }

    protected abstract double calculateFor(ShoppingCard shoppingCard, DeliveryCostDTO deliveryCostDTO, int numberOfDeliveries);

    @Override
    public double getCouponDiscount(ShoppingCard shoppingCard) {
        return calculateCouponDiscount(shoppingCard);
    }

    protected abstract double calculateCouponDiscount(ShoppingCard shoppingCard);

    @Override
    public double getCampaignDiscount(ShoppingCard shoppingCard) {
        return calculateCampaignDiscount(shoppingCard);
    }

    protected abstract double calculateCampaignDiscount(ShoppingCard shoppingCard);

    @Override
    public double getTotalAmountAfterDiscounts(ShoppingCard shoppingCard) {
        return calculateTotalAmountAfterDiscounts(shoppingCard);
    }

    protected abstract double calculateTotalAmountAfterDiscounts(ShoppingCard shoppingCard);

}
