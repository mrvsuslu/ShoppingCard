package com.system.shoppingcard.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private long id;

    @Column
    private String title;

    @ManyToOne
    @JoinColumn(table = "category", name = "parent_category_id")
    private Category parentCategory;

    public Category() {
    }
}
