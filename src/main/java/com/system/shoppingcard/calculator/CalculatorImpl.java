package com.system.shoppingcard.calculator;

import com.system.shoppingcard.domain.*;
import com.system.shoppingcard.dto.DeliveryCostDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CalculatorImpl extends CalculatorAbst {

    @Override
    protected double calculateFor(ShoppingCard cart, DeliveryCostDTO deliveryCostDTO, int numberOfDeliveries) {
        return (deliveryCostDTO.getCostPerDelivery() * numberOfDeliveries) +
                (deliveryCostDTO.getCostPerProduct() * cart.getProducts().size()) + deliveryCostDTO.getFixedCost();
    }

    @Override
    protected double calculateCouponDiscount(ShoppingCard shoppingCard) {
        return shoppingCard.getTotalPriceWithCampaign() - shoppingCard.getTotalPriceWithCoupon();
    }

    @Override
    protected double calculateCampaignDiscount(ShoppingCard shoppingCard) {
        return shoppingCard.getTotalPriceWithoutDiscount() - shoppingCard.getTotalPriceWithCampaign();
    }

    @Override
    protected double calculateTotalAmountAfterDiscounts(ShoppingCard shoppingCard) {
        return shoppingCard.getTotalPriceWithCoupon();
    }

    @Override
    protected ShoppingCard applyCouponDiscount(ShoppingCard shoppingCard, Coupon coupon) {
        if (shoppingCard.getTotalPriceWithCampaign() < coupon.getMinimumPurchaseAmount())
            throw new IllegalArgumentException("Cart total amount can not be less than coupon minimum amount.");

        double totalPriceWithCampaign = shoppingCard.getTotalPriceWithCampaign();
        DiscountType discountType = coupon.getDiscountType();
        double discountRate = coupon.getDiscountRate();

        if (discountType.equals(DiscountType.RATE)) {
            shoppingCard.setTotalPriceWithCoupon(totalPriceWithCampaign -
                    ((totalPriceWithCampaign * discountRate) / 100));
        } else if (discountType.equals(DiscountType.AMOUNT)) {
            shoppingCard.setTotalPriceWithCoupon(totalPriceWithCampaign - discountRate);
        }

        return shoppingCard;
    }

    @Override
    protected ShoppingCard applyCampaignDiscount(ShoppingCard shoppingCard, List<Campaign> campaignList) {
        double totalPriceWithDiscount = 0;

        for (Map.Entry<Product, Long> item : shoppingCard.getProducts().entrySet()) {
            double itemCost = item.getKey().getPrice() * item.getValue();
            for (Campaign campaign : campaignList) {
                Optional<Category> category = campaign.getCategories().stream()
                        .filter(e -> item.getKey().getCategory().getId() == (e.getId()))
                        .findAny();
                if (category.isPresent()) {
                    DiscountType discountType = campaign.getDiscountType();
                    double discountRate = campaign.getDiscount();
                    double purchasedItem = campaign.getPurchasedItem();

                    if (item.getValue() > purchasedItem) {
                        if (discountType.equals(DiscountType.RATE)) {
                            itemCost = itemCost - ((itemCost * discountRate) / 100);
                        } else if (discountType.equals(DiscountType.AMOUNT)) {
                            itemCost = itemCost - discountRate;
                        }
                    }
                }
            }

            totalPriceWithDiscount += itemCost;
        }

        shoppingCard.setTotalPriceWithCampaign(totalPriceWithDiscount);

        return shoppingCard;
    }
}
