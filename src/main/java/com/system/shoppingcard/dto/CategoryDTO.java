package com.system.shoppingcard.dto;

import lombok.Data;

@Data
public class CategoryDTO {

    private String title;
    private long parentCategoryId;

}
