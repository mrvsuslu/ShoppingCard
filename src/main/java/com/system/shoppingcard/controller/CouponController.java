package com.system.shoppingcard.controller;

import com.system.shoppingcard.authentication.ShoppingCardAuthService;
import com.system.shoppingcard.domain.Coupon;
import com.system.shoppingcard.dto.CouponDTO;
import com.system.shoppingcard.messageProvider.LocaleMessageProvider;
import com.system.shoppingcard.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    private final ShoppingCardAuthService shoppingCardAuthService;

    private final LocaleMessageProvider localeMessageProvider;

    @PostMapping(value = "/add")
    @ResponseBody
    public ResponseEntity addCoupon(@RequestHeader HttpHeaders httpHeaders, @RequestBody CouponDTO coupon) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        couponService.add(coupon);
        return new ResponseEntity<>(localeMessageProvider.getMessage("success.coupon.add"), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}")
    @ResponseBody
    public ResponseEntity updateCoupon(@RequestHeader HttpHeaders httpHeaders, @RequestBody CouponDTO coupon, @PathVariable("id") final long id) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        couponService.update(coupon, id);
        return new ResponseEntity<>(localeMessageProvider.getMessage("success.coupon.update"), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    @ResponseBody
    public ResponseEntity deleteCoupon(@RequestHeader HttpHeaders httpHeaders, @PathVariable("id") final long id) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        couponService.delete(id);
        return new ResponseEntity<>(localeMessageProvider.getMessage("success.coupon.delete"), HttpStatus.OK);
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Coupon>> getAllCoupons(@RequestHeader HttpHeaders httpHeaders) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        return new ResponseEntity<>(couponService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<Coupon> getCoupon(@RequestHeader HttpHeaders httpHeaders, @PathVariable("id") final long id) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        return new ResponseEntity<>(couponService.get(id), HttpStatus.OK);
    }

}
