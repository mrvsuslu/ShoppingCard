package com.system.shoppingcard.integrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.shoppingcard.domain.Coupon;
import com.system.shoppingcard.domain.DiscountType;
import com.system.shoppingcard.messageProvider.LocaleMessageProvider;
import com.system.shoppingcard.repository.CouponRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CouponControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponRepository couponRepository;

    @Autowired
    private LocaleMessageProvider localeMessageProvider;

    private JacksonTester<Coupon> jsonCoupon;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void createCoupon() throws Exception {
        Coupon coupon = new Coupon();
        coupon.setId(1L);
        coupon.setName("DC6312");
        coupon.setDiscountRate(12.0);
        coupon.setMinimumPurchaseAmount(100.0);
        coupon.setDiscountType(DiscountType.AMOUNT);

        // given
        given(couponRepository.save(coupon)).willReturn(coupon);

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                post("/coupon/add")
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON).content(
                        jsonCoupon.write(coupon).getJson()
                )).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertEquals(response.getContentAsString(), localeMessageProvider.getMessage("success.coupon.add"));
    }


    @Test
    public void updateCoupon() throws Exception {
        Coupon coupon = new Coupon();
        coupon.setId(1L);
        coupon.setName("DC6312");
        coupon.setDiscountRate(12.0);
        coupon.setMinimumPurchaseAmount(100.0);
        coupon.setDiscountType(DiscountType.AMOUNT);

        // given
        given(couponRepository.findById(coupon.getId())).willReturn(Optional.of(coupon));

        Coupon newCoupon = new Coupon();
        newCoupon.setId(1L);
        newCoupon.setName("DC6312");
        newCoupon.setDiscountRate(14.0);
        newCoupon.setMinimumPurchaseAmount(120.0);
        newCoupon.setDiscountType(DiscountType.AMOUNT);

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                put("/coupon/update/" + coupon.getId())
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON).content(
                        jsonCoupon.write(newCoupon).getJson()
                )).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertEquals(response.getContentAsString(), localeMessageProvider.getMessage("success.coupon.update"));
    }

    @Test
    public void getAllCategories() throws Exception {
        Coupon coupon = new Coupon();
        coupon.setId(1L);
        coupon.setName("DC6312");
        coupon.setDiscountRate(12.0);
        coupon.setMinimumPurchaseAmount(100.0);
        coupon.setDiscountType(DiscountType.AMOUNT);

        // given
        given(couponRepository.save(coupon)).willReturn(coupon);

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                get("/coupon/getAll")
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void getCoupon() throws Exception {
        Coupon coupon = new Coupon();
        coupon.setId(1L);
        coupon.setName("DC6312");
        coupon.setDiscountRate(12.0);
        coupon.setMinimumPurchaseAmount(100.0);
        coupon.setDiscountType(DiscountType.AMOUNT);

        // given
        given(couponRepository.findById(coupon.getId())).willReturn(Optional.of(coupon));

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                get("/coupon/get/" + coupon.getId())
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    public void deleteCoupon() throws Exception {
        Coupon coupon = new Coupon();
        coupon.setId(1L);
        coupon.setName("DC6312");
        coupon.setDiscountRate(12.0);
        coupon.setMinimumPurchaseAmount(100.0);
        coupon.setDiscountType(DiscountType.AMOUNT);

        // given
        given(couponRepository.save(coupon)).willReturn(coupon);

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                delete("/coupon/delete/" + coupon.getId())
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertEquals(response.getContentAsString(), localeMessageProvider.getMessage("success.coupon.delete"));
    }
}