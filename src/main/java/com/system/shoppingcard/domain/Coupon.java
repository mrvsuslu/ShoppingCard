package com.system.shoppingcard.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private long id;

    @Column(unique = true)
    private String name;

    @Column
    private double minimumPurchaseAmount;

    @Column
    private double discountRate;

    @Enumerated(EnumType.STRING)
    @Column
    private DiscountType discountType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_cart")
    private ShoppingCard shoppingcard;

    public Coupon() {
    }

}
