package com.system.shoppingcard.unitTests;

import com.system.shoppingcard.domain.*;
import com.system.shoppingcard.dto.CampaignDTO;
import com.system.shoppingcard.dto.CouponDTO;
import com.system.shoppingcard.dto.ShoppingCardDTO;
import com.system.shoppingcard.repository.ShoppingCardRepository;
import com.system.shoppingcard.service.CampaignService;
import com.system.shoppingcard.service.CouponService;
import com.system.shoppingcard.service.ProductService;
import com.system.shoppingcard.service.ShoppingCardServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ShoppingCardServiceImplTest {

    @Mock
    private ShoppingCardRepository shoppingCardRepository;

    @Mock
    private CouponService couponService;

    @Mock
    private CampaignService campaignService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ShoppingCardServiceImpl shoppingCardService;

    @Test
    public void testAdd() {
        shoppingCardService.add();
        verify(shoppingCardRepository, times(1)).save(any(ShoppingCard.class));
    }

    @Test
    public void testAddItem() {

        ArgumentCaptor<ShoppingCard> shoppingCardCaptor = ArgumentCaptor.forClass(ShoppingCard.class);

        Product product = new Product();
        product.setPrice(200);
        ShoppingCard shoppingCard = new ShoppingCard();
        shoppingCard.setTotalPriceWithoutDiscount(0);

        ShoppingCardDTO shoppingCardDTO = new ShoppingCardDTO();
        long quantity = 1L;
        shoppingCardDTO.setQuantity(quantity);
        long shoppingCardId = 2L;
        shoppingCardDTO.setShoppingCardId(shoppingCardId);
        long productId = 3L;
        shoppingCardDTO.setProductId(productId);

        when(productService.get(productId)).thenReturn(product);
        when(shoppingCardRepository.findById(any())).thenReturn(Optional.of(shoppingCard));

        shoppingCardService.addItem(shoppingCardDTO);
        verify(shoppingCardRepository).findById(shoppingCardId);
        verify(shoppingCardRepository).save(shoppingCardCaptor.capture());

        ShoppingCard shoppingCardToValidate = shoppingCardCaptor.getValue();

        double expectedPrice = (product.getPrice() * shoppingCardDTO.getQuantity());

        long quantityToValidate = shoppingCardToValidate.getProducts().get(product);
        assertEquals("shoppingCardDto quantity and product quantity should be equal", quantity, quantityToValidate);
        assertEquals("Total price without discount should be calculated properly", expectedPrice, shoppingCard.getTotalPriceWithoutDiscount(), 1);
    }

    @Test
    public void removeItem() {

        long quantity = 0L;
        long shoppingCardId = 2L;
        long productId = 0L;

        ShoppingCardDTO shoppingCardDTO = new ShoppingCardDTO();
        shoppingCardDTO.setQuantity(quantity);
        shoppingCardDTO.setShoppingCardId(shoppingCardId);
        shoppingCardDTO.setProductId(productId);

        ShoppingCard shoppingCard = new ShoppingCard();
        shoppingCard.setId(shoppingCardId);
        Product product = new Product();
        product.setId(productId);

        shoppingCard.getProducts().put(product, 0L);

        when(productService.get(productId)).thenReturn(product);
        when(shoppingCardRepository.findById(any())).thenReturn(Optional.of(shoppingCard));

        shoppingCardService.removeItem(shoppingCardDTO);
        verify(shoppingCardRepository).save(shoppingCard);
        verify(shoppingCardRepository).deleteById(shoppingCard.getId());
        verify(shoppingCardRepository).findById(shoppingCardDTO.getShoppingCardId());
    }

    @Test
    public void removeAll() {
        shoppingCardService.removeAll();
        verify(shoppingCardRepository).deleteAll();
    }

    @Test
    public void getAll() {
        shoppingCardService.getAll();
        verify(shoppingCardRepository).findAll();
    }

    @Test
    public void get() {
        long expectedId = 1L;
        when(shoppingCardRepository.findById(expectedId)).thenReturn(Optional.of(new ShoppingCard()));
        shoppingCardService.get(expectedId);
        verify(shoppingCardRepository).findById(Mockito.eq(expectedId));
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyCouponsShouldThrowIllegalArgumentException() {

        ArgumentCaptor<ShoppingCard> shoppingCardCaptor = ArgumentCaptor.forClass(ShoppingCard.class);

        Category category = new Category();
        category.setId(1);

        Product product = new Product();
        product.setPrice(100);
        product.setCategory(category);

        ShoppingCardDTO shoppingCardDTO = new ShoppingCardDTO();
        long quantity = 2L;
        shoppingCardDTO.setQuantity(quantity);
        long shoppingCardId = 2L;
        shoppingCardDTO.setShoppingCardId(shoppingCardId);
        long productId = 3L;
        shoppingCardDTO.setProductId(productId);

        ShoppingCard shoppingCard = new ShoppingCard();
        shoppingCard.setTotalPriceWithoutDiscount(200);
        shoppingCard.setTotalPriceWithCampaign(200);
        shoppingCard.setTotalPriceWithCoupon(200);
        shoppingCard.getProducts().put(product, quantity);

        CouponDTO couponDTO = new CouponDTO();
        couponDTO.setDiscountRate(10);
        couponDTO.setDiscountType("AMOUNT");
        couponDTO.setMinimumPurchaseAmount(100);

        Coupon coupon = new Coupon();
        long couponId = 1L;
        coupon.setId(couponId);
        coupon.setDiscountType(DiscountType.AMOUNT);
        coupon.setDiscountRate(10.0);
        coupon.setMinimumPurchaseAmount(100);
        coupon.setName("DC6312");

        shoppingCard.getCoupons().add(coupon);
        shoppingCard.setAppliedDiscounts(true);

        couponService.add(couponDTO);
        when(productService.get(productId)).thenReturn(product);
        when(couponService.get(couponId)).thenReturn(coupon);
        when(couponService.getByName(coupon.getName())).thenReturn(coupon);
        when(shoppingCardRepository.findById(any())).thenReturn(Optional.of(shoppingCard));

        shoppingCardService.applyDiscounts(shoppingCardDTO);
        shoppingCardService.applyCoupon(shoppingCardDTO, coupon.getName());
        verify(shoppingCardRepository).findById(Mockito.eq(shoppingCardId));
        verify(shoppingCardRepository).save(shoppingCardCaptor.capture());

        double expectedPrice = shoppingCard.getTotalPriceWithCampaign() - coupon.getDiscountRate();

        assertEquals("Total price with coupon should be calculated properly", expectedPrice, shoppingCard.getTotalPriceWithCoupon(), 1);

    }

    @Test(expected = IllegalArgumentException.class)
    public void applyCouponsBeforeCampaignsShouldThrowIllegalArgumentException() {

        ArgumentCaptor<ShoppingCard> shoppingCardCaptor = ArgumentCaptor.forClass(ShoppingCard.class);

        Category category = new Category();
        category.setId(1);

        Product product = new Product();
        product.setPrice(100);
        product.setCategory(category);

        ShoppingCardDTO shoppingCardDTO = new ShoppingCardDTO();
        long quantity = 2L;
        shoppingCardDTO.setQuantity(quantity);
        long shoppingCardId = 2L;
        shoppingCardDTO.setShoppingCardId(shoppingCardId);
        long productId = 3L;
        shoppingCardDTO.setProductId(productId);

        ShoppingCard shoppingCard = new ShoppingCard();
        shoppingCard.setTotalPriceWithoutDiscount(200);
        shoppingCard.setTotalPriceWithCampaign(200);
        shoppingCard.setTotalPriceWithCoupon(200);
        shoppingCard.getProducts().put(product, quantity);

        CouponDTO couponDTO = new CouponDTO();
        couponDTO.setDiscountRate(10);
        couponDTO.setDiscountType("AMOUNT");
        couponDTO.setMinimumPurchaseAmount(100);

        Coupon coupon = new Coupon();
        long couponId = 1L;
        coupon.setId(couponId);
        coupon.setDiscountType(DiscountType.AMOUNT);
        coupon.setDiscountRate(10.0);
        coupon.setMinimumPurchaseAmount(100);
        coupon.setName("DC6312");

        shoppingCard.setAppliedDiscounts(false);

        couponService.add(couponDTO);
        when(productService.get(productId)).thenReturn(product);
        when(couponService.get(couponId)).thenReturn(coupon);
        when(couponService.getByName(coupon.getName())).thenReturn(coupon);
        when(shoppingCardRepository.findById(any())).thenReturn(Optional.of(shoppingCard));

        shoppingCardService.applyCoupon(shoppingCardDTO, coupon.getName());
        verify(shoppingCardRepository).findById(Mockito.eq(shoppingCardId));
        verify(shoppingCardRepository).save(shoppingCardCaptor.capture());

        double expectedPrice = shoppingCard.getTotalPriceWithCampaign() - coupon.getDiscountRate();

        assertEquals("Total price with coupon should be calculated properly", expectedPrice, shoppingCard.getTotalPriceWithCoupon(), 1);

    }

    @Test
    public void applyDiscounts() {

        ArgumentCaptor<ShoppingCard> shoppingCardCaptor = ArgumentCaptor.forClass(ShoppingCard.class);

        Category category = new Category();
        category.setId(1);

        Product product = new Product();
        product.setPrice(100);
        product.setCategory(category);

        ShoppingCardDTO shoppingCardDTO = new ShoppingCardDTO();
        long quantity = 2L;
        shoppingCardDTO.setQuantity(quantity);
        long shoppingCardId = 2L;
        shoppingCardDTO.setShoppingCardId(shoppingCardId);
        long productId = 3L;
        shoppingCardDTO.setProductId(productId);

        ShoppingCard shoppingCard = new ShoppingCard();
        shoppingCard.setTotalPriceWithoutDiscount(200);
        shoppingCard.setTotalPriceWithCampaign(200);
        shoppingCard.getProducts().put(product, quantity);

        CampaignDTO campaignDTO = new CampaignDTO();
        campaignDTO.setDiscount(10);
        campaignDTO.setDiscountType("RATE");
        campaignDTO.setPurchasedItem(1);
        campaignDTO.setCategoryId(1);

        Campaign campaign = new Campaign();
        long campaignId = 1L;
        campaign.setId(campaignId);

        campaignService.add(campaignDTO);
        when(productService.get(productId)).thenReturn(product);
        when(campaignService.get(campaignId)).thenReturn(campaign);
        when(shoppingCardRepository.findById(any())).thenReturn(Optional.of(shoppingCard));

        shoppingCardService.applyDiscounts(shoppingCardDTO);
        verify(shoppingCardRepository).findById(Mockito.eq(shoppingCardId));
        verify(shoppingCardRepository).save(shoppingCardCaptor.capture());

        double unitPrice = (product.getPrice() * shoppingCardDTO.getQuantity());
        double expectedPrice = unitPrice - (unitPrice * campaign.getDiscount()) / 100;

        assertEquals("Total price with campaign should be calculated properly", expectedPrice, shoppingCard.getTotalPriceWithCampaign(), 1);

    }

    @Test(expected = IllegalArgumentException.class)
    public void getShouldThrowAnException() {
        shoppingCardService.get(555);
    }
}