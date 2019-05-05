package com.system.shoppingcard.service;

import com.system.shoppingcard.domain.Coupon;
import com.system.shoppingcard.dto.CouponDTO;

public interface CouponService extends DiscountService<Coupon, CouponDTO> {

    Coupon getByName(String name);

}
