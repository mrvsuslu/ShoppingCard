package com.system.shoppingcard.service;

import com.system.shoppingcard.domain.ShoppingCard;
import com.system.shoppingcard.dto.DeliveryCostDTO;
import com.system.shoppingcard.dto.ShoppingCardDTO;
import com.system.shoppingcard.dto.ShoppingResultDTO;

import java.util.List;

public interface ShoppingCardService {

    void add();

    void addItem(ShoppingCardDTO shoppingCardDTO);

    void removeItem(ShoppingCardDTO shoppingCardDTO);

    void removeAll();

    List<ShoppingCard> getAll();

    ShoppingCard get(long id);

    void applyCoupon(ShoppingCardDTO shoppingCardDTO, String couponName);

    void applyDiscounts(ShoppingCardDTO shoppingCardDTO);

    double getCouponDiscount(ShoppingCardDTO shoppingCardDTO);

    double getCampaignDiscount(ShoppingCardDTO shoppingCardDTO);

    double getTotalAmountAfterDiscounts(ShoppingCardDTO shoppingCardDTO);

    double getDeliveryCost(DeliveryCostDTO deliveryCost);

    ShoppingResultDTO print(DeliveryCostDTO deliveryCostDTO);

}
