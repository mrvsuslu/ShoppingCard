package com.system.shoppingcard.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
public class ShoppingCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shoppingcard_id")
    private long id;

    @Column(name = "totalPriceWithoutDiscount")
    private double totalPriceWithoutDiscount;

    @Column(name = "totalPriceWithCampaign")
    private double totalPriceWithCampaign;

    @Column(name = "totalPriceWithCoupon")
    private double totalPriceWithCoupon;

    @Column(name = "appliedDiscounts")
    private boolean appliedDiscounts;

    @OneToMany(targetEntity = Product.class, mappedBy = "shoppingcard", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapKeyColumn(name = "product")
    private Map<Product, Long> products = new HashMap<>();

    @OneToMany(targetEntity = Coupon.class, mappedBy = "shoppingcard", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Coupon> coupons = new ArrayList<>();

    public ShoppingCard() {
    }

}
