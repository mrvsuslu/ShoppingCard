package com.system.shoppingcard.controller;

import com.system.shoppingcard.authentication.ShoppingCardAuthService;
import com.system.shoppingcard.domain.ShoppingCard;
import com.system.shoppingcard.dto.DeliveryCostDTO;
import com.system.shoppingcard.dto.ShoppingCardDTO;
import com.system.shoppingcard.dto.ShoppingResultDTO;
import com.system.shoppingcard.messageProvider.LocaleMessageProvider;
import com.system.shoppingcard.service.ShoppingCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingcard")
@RequiredArgsConstructor
public class ShoppingCardController {

    private final ShoppingCardService shoppingCardService;
    private final ShoppingCardAuthService shoppingCardAuthService;

    private final LocaleMessageProvider localeMessageProvider;

    @PostMapping(value = "/add")
    @ResponseBody
    public ResponseEntity<String> add(@RequestHeader HttpHeaders httpHeaders) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        shoppingCardService.add();
        return new ResponseEntity<>(localeMessageProvider.getMessage("success.shoppingCard.created"), HttpStatus.OK);
    }

    @PostMapping(value = "/addItem")
    @ResponseBody
    public ResponseEntity<String> addItem(@RequestHeader HttpHeaders httpHeaders, @RequestBody ShoppingCardDTO shoppingCard) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        shoppingCardService.addItem(shoppingCard);
        return new ResponseEntity<>(localeMessageProvider.getMessage("success.shoppingCard.addItem"), HttpStatus.OK);
    }

    @PutMapping(value = "/removeItem/{quantity}")
    @ResponseBody
    public ResponseEntity<String> removeItem(@RequestHeader HttpHeaders httpHeaders, @RequestBody ShoppingCardDTO shoppingCard, @PathVariable("quantity") int quantity) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        shoppingCardService.removeItem(shoppingCard);
        return new ResponseEntity<>(localeMessageProvider.getMessage("success.shoppingCard.removeItem"), HttpStatus.OK);
    }

    @DeleteMapping(value = "/removeAllItems")
    @ResponseBody
    public ResponseEntity<String> removeAllItems(@RequestHeader HttpHeaders httpHeaders) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        shoppingCardService.removeAll();
        return new ResponseEntity<>(localeMessageProvider.getMessage("success.shoppingCard.removeAll"), HttpStatus.OK);
    }

    @GetMapping(value = "/getAllItems")
    @ResponseBody
    public ResponseEntity<List<ShoppingCard>> getAllItems(@RequestHeader HttpHeaders httpHeaders) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        return new ResponseEntity<>(shoppingCardService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<ShoppingCard> getProduct(@RequestHeader HttpHeaders httpHeaders, @PathVariable("id") long id) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        return new ResponseEntity<>(shoppingCardService.get(id), HttpStatus.OK);
    }

    @PutMapping(value = "/applyCoupon/{couponName}")
    @ResponseBody
    public ResponseEntity<String> applyCoupon(@RequestHeader HttpHeaders httpHeaders, @RequestBody ShoppingCardDTO shoppingCard,
                                              @PathVariable("couponName") String couponName) {

        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        shoppingCardService.applyCoupon(shoppingCard, couponName);
        return new ResponseEntity<String>(localeMessageProvider.getMessage("success.shoppingCard.applyCoupon"), HttpStatus.OK);
    }

    @PutMapping(value = "/applyDiscounts")
    @ResponseBody
    public ResponseEntity<String> applyDiscounts(@RequestHeader HttpHeaders httpHeaders, @RequestBody ShoppingCardDTO shoppingCard) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        shoppingCardService.applyDiscounts(shoppingCard);
        return new ResponseEntity<String>(localeMessageProvider.getMessage("success.shoppingCard.applyDiscounts"), HttpStatus.OK);
    }

    @GetMapping(value = "/getCouponDiscount")
    @ResponseBody
    public ResponseEntity<Double> getCouponDiscount(@RequestHeader HttpHeaders httpHeaders, @RequestBody ShoppingCardDTO shoppingCard) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        return new ResponseEntity<>(shoppingCardService.getCouponDiscount(shoppingCard), HttpStatus.OK);
    }

    @GetMapping(value = "/getCampaignDiscount")
    @ResponseBody
    public ResponseEntity<Double> getCampaignDiscount(@RequestHeader HttpHeaders httpHeaders, @RequestBody ShoppingCardDTO shoppingCard) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        return new ResponseEntity<>(shoppingCardService.getCampaignDiscount(shoppingCard), HttpStatus.OK);
    }

    @GetMapping(value = "/getTotalAmountAfterDiscounts")
    @ResponseBody
    public ResponseEntity<Double> getTotalAmountAfterDiscounts(@RequestHeader HttpHeaders httpHeaders, @RequestBody ShoppingCardDTO shoppingCard) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        return new ResponseEntity<>(shoppingCardService.getTotalAmountAfterDiscounts(shoppingCard), HttpStatus.OK);
    }

    @GetMapping(value = "/getDeliveryCost")
    @ResponseBody
    public ResponseEntity<Double> getDeliveryCost(@RequestHeader HttpHeaders httpHeaders, @RequestBody DeliveryCostDTO deliveryCost) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        return new ResponseEntity<>(shoppingCardService.getDeliveryCost(deliveryCost), HttpStatus.OK);
    }

    @GetMapping(value = "/print")
    @ResponseBody
    public ResponseEntity<ShoppingResultDTO> print(@RequestHeader HttpHeaders httpHeaders, @RequestBody DeliveryCostDTO deliveryCost) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        return new ResponseEntity<>(shoppingCardService.print(deliveryCost), HttpStatus.OK);
    }
}
