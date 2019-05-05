package com.system.shoppingcard.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany(targetEntity = Category.class, fetch = FetchType.LAZY)
    @JoinTable(name = "Category_Campaign", joinColumns = {@JoinColumn(name = "category_id")}, inverseJoinColumns = {
            @JoinColumn(name = "campaign_id")})
    private Set<Category> categories = new HashSet<>();

    @Column
    private double discount;

    @Column
    private double purchasedItem;

    @Column
    private DiscountType discountType;

    public Campaign() {
    }

}
