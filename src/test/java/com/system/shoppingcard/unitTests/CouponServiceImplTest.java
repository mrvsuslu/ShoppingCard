package com.system.shoppingcard.unitTests;

import com.system.shoppingcard.domain.Coupon;
import com.system.shoppingcard.domain.DiscountType;
import com.system.shoppingcard.dto.CouponDTO;
import com.system.shoppingcard.repository.CouponRepository;
import com.system.shoppingcard.service.CouponServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CouponServiceImplTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponServiceImpl couponService;

    @Test
    public void testAdd() {

        ArgumentCaptor<Coupon> couponCaptor = ArgumentCaptor.forClass(Coupon.class);

        Coupon coupon = new Coupon();
        coupon.setDiscountType(DiscountType.AMOUNT);

        CouponDTO couponDTO = new CouponDTO();
        long discountRate = 2L;
        couponDTO.setDiscountRate(discountRate);
        couponDTO.setName("DC6312");
        double minPurchaseAmount = 100L;
        couponDTO.setMinimumPurchaseAmount(minPurchaseAmount);
        couponDTO.setDiscountType("AMOUNT");

        when(couponRepository.findById(any())).thenReturn(Optional.of(coupon));

        couponService.add(couponDTO);
        verify(couponRepository).save(couponCaptor.capture());
    }

    @Test
    public void testUpdate() {

        ArgumentCaptor<Coupon> couponCaptor = ArgumentCaptor.forClass(Coupon.class);

        Coupon coupon = new Coupon();
        coupon.setDiscountType(DiscountType.AMOUNT);
        long couponId = 1L;

        CouponDTO couponDTO = new CouponDTO();
        long discountRate = 2L;
        couponDTO.setDiscountRate(discountRate);
        couponDTO.setName("DC6312");
        double minPurchaseAmount = 100L;
        couponDTO.setMinimumPurchaseAmount(minPurchaseAmount);
        couponDTO.setDiscountType("AMOUNT");

        when(couponRepository.findById(any())).thenReturn(Optional.of(coupon));

        couponService.update(couponDTO, couponId);
        verify(couponRepository).save(couponCaptor.capture());
    }


    @Test
    public void delete() {

        CouponDTO couponDTO = new CouponDTO();
        long discountRate = 2L;
        couponDTO.setDiscountRate(discountRate);
        couponDTO.setName("DC6312");
        double minPurchaseAmount = 100L;
        couponDTO.setMinimumPurchaseAmount(minPurchaseAmount);
        couponDTO.setDiscountType("AMOUNT");

        long couponId = 1L;
        Coupon coupon = new Coupon();
        coupon.setId(couponId);

        when(couponRepository.findById(any())).thenReturn(Optional.of(coupon));

        couponService.delete(couponId);
        verify(couponRepository).deleteById(coupon.getId());
    }

    @Test
    public void getAll() {
        couponService.getAll();
        verify(couponRepository).findAll();
    }

    @Test
    public void get() {
        long expectedId = 1L;
        when(couponRepository.findById(expectedId)).thenReturn(Optional.of(new Coupon()));
        couponService.get(expectedId);
        verify(couponRepository).findById(Mockito.eq(expectedId));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getShouldThrowAnException() {
        couponService.get(555);
    }
}