package com.system.shoppingcard.integrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.shoppingcard.domain.*;
import com.system.shoppingcard.dto.DeliveryCostDTO;
import com.system.shoppingcard.dto.ShoppingCardDTO;
import com.system.shoppingcard.messageProvider.LocaleMessageProvider;
import com.system.shoppingcard.repository.ProductRepository;
import com.system.shoppingcard.repository.ShoppingCardRepository;
import com.system.shoppingcard.service.CampaignService;
import com.system.shoppingcard.service.CategoryService;
import com.system.shoppingcard.service.CouponService;
import com.system.shoppingcard.service.ShoppingCardService;
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

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ShoppingCardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingCardRepository shoppingcardRepository;

    @MockBean
    private ShoppingCardService shoppingCardService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CouponService couponService;

    @MockBean
    private CampaignService campaignService;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private LocaleMessageProvider localeMessageProvider;

    private JacksonTester<ShoppingCard> jsonShoppingCard;

    private JacksonTester<DeliveryCostDTO> jsonDeliveryCost;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void createShoppingCard() throws Exception {
        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                post("/shoppingcard/add")
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertEquals(response.getContentAsString(), localeMessageProvider.getMessage("success.shoppingCard.created"));
    }

    @Test
    public void addItemToShoppingCard() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("food");

        Product product = new Product();
        product.setId(1L);
        product.setTitle("apple");
        product.setCategory(category);
        product.setPrice(10.0);

        ShoppingCardDTO shoppingcardDTO = new ShoppingCardDTO();
        shoppingcardDTO.setProductId(product.getId());
        shoppingcardDTO.setQuantity(3L);
        shoppingcardDTO.setShoppingCardId(1L);

        Map<Product, Long> products = new HashMap<>();
        products.put(product, 3L);

        ShoppingCard shoppingcard = new ShoppingCard();
        shoppingcard.setId(1L);
        shoppingcard.setTotalPriceWithCoupon(30.0);
        shoppingcard.setTotalPriceWithCampaign(30.0);
        shoppingcard.setTotalPriceWithoutDiscount(30.0);
        shoppingcard.setProducts(products);

        // given
        given(categoryService.get(category.getId())).willReturn(category);
        given(productRepository.save(product)).willReturn(product);
        given(shoppingCardService.get(shoppingcardDTO.getShoppingCardId())).willReturn(shoppingcard);

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                post("/shoppingcard/addItem")
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON).content(
                        jsonShoppingCard.write(shoppingcard).getJson()
                )).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertEquals(response.getContentAsString(), localeMessageProvider.getMessage("success.shoppingCard.addItem"));
    }

    @Test
    public void removeItemFromShoppingCard() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("food");

        Product product = new Product();
        product.setId(1L);
        product.setTitle("apple");
        product.setCategory(category);
        product.setPrice(10.0);

        ShoppingCardDTO shoppingcardDTO = new ShoppingCardDTO();
        shoppingcardDTO.setProductId(product.getId());
        shoppingcardDTO.setQuantity(3L);
        shoppingcardDTO.setShoppingCardId(1L);

        Map<Product, Long> products = new HashMap<>();
        products.put(product, 3L);

        ShoppingCard shoppingcard = new ShoppingCard();
        shoppingcard.setId(1L);
        shoppingcard.setTotalPriceWithCoupon(30.0);
        shoppingcard.setTotalPriceWithCampaign(30.0);
        shoppingcard.setTotalPriceWithoutDiscount(30.0);
        shoppingcard.setProducts(products);

        // given
        given(categoryService.get(category.getId())).willReturn(category);
        given(productRepository.save(product)).willReturn(product);
        given(shoppingCardService.get(shoppingcardDTO.getShoppingCardId())).willReturn(shoppingcard);

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                put("/shoppingcard/removeItem/2")
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON).content(
                        jsonShoppingCard.write(shoppingcard).getJson()
                )).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertEquals(response.getContentAsString(), localeMessageProvider.getMessage("success.shoppingCard.removeItem"));
    }

    @Test
    public void getAllItems() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("food");

        Product product = new Product();
        product.setId(1L);
        product.setTitle("apple");
        product.setCategory(category);
        product.setPrice(10.0);

        Map<Product, Long> products = new HashMap<>();
        products.put(product, 3L);

        ShoppingCard shoppingcard = new ShoppingCard();
        shoppingcard.setId(1L);
        shoppingcard.setTotalPriceWithCoupon(30.0);
        shoppingcard.setTotalPriceWithCampaign(30.0);
        shoppingcard.setTotalPriceWithoutDiscount(30.0);
        shoppingcard.setProducts(products);

        // given
        given(shoppingcardRepository.save(shoppingcard)).willReturn(shoppingcard);

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                get("/shoppingcard/getAllItems")
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void getShoppingCardById() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("food");

        Product product = new Product();
        product.setId(1L);
        product.setTitle("apple");
        product.setCategory(category);
        product.setPrice(10.0);

        ShoppingCardDTO shoppingcardDTO = new ShoppingCardDTO();
        shoppingcardDTO.setProductId(product.getId());
        shoppingcardDTO.setQuantity(3L);
        shoppingcardDTO.setShoppingCardId(1L);

        Map<Product, Long> products = new HashMap<>();
        products.put(product, 3L);

        ShoppingCard shoppingcard = new ShoppingCard();
        shoppingcard.setId(1L);
        shoppingcard.setTotalPriceWithCoupon(30.0);
        shoppingcard.setTotalPriceWithCampaign(30.0);
        shoppingcard.setTotalPriceWithoutDiscount(30.0);
        shoppingcard.setProducts(products);

        // given
        given(categoryService.get(category.getId())).willReturn(category);
        given(productRepository.save(product)).willReturn(product);
        given(shoppingCardService.get(shoppingcardDTO.getShoppingCardId())).willReturn(shoppingcard);

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                get("/shoppingcard/get/" + shoppingcard.getId())
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON)).
                andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void removeAllItems() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("food");

        Product product = new Product();
        product.setId(1L);
        product.setTitle("apple");
        product.setCategory(category);
        product.setPrice(10.0);

        Map<Product, Long> products = new HashMap<>();
        products.put(product, 3L);

        ShoppingCard shoppingcard = new ShoppingCard();
        shoppingcard.setId(1L);
        shoppingcard.setTotalPriceWithCoupon(30.0);
        shoppingcard.setTotalPriceWithCampaign(30.0);
        shoppingcard.setTotalPriceWithoutDiscount(30.0);
        shoppingcard.setProducts(products);

        // given
        given(shoppingcardRepository.save(shoppingcard)).willReturn(shoppingcard);

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                delete("/shoppingcard/removeAllItems")
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void applyCoupon() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("food");

        Product product = new Product();
        product.setId(1L);
        product.setTitle("apple");
        product.setCategory(category);
        product.setPrice(10.0);

        Map<Product, Long> products = new HashMap<>();
        products.put(product, 3L);

        Coupon coupon = new Coupon();
        coupon.setId(1L);
        coupon.setName("DC6312");
        coupon.setDiscountRate(12.0);
        coupon.setMinimumPurchaseAmount(100.0);
        coupon.setDiscountType(DiscountType.AMOUNT);

        ShoppingCard shoppingcard = new ShoppingCard();
        shoppingcard.setId(1L);
        shoppingcard.setTotalPriceWithCoupon(30.0);
        shoppingcard.setTotalPriceWithCampaign(30.0);
        shoppingcard.setTotalPriceWithoutDiscount(30.0);
        shoppingcard.setProducts(products);

        // given
        given(couponService.get(coupon.getId())).willReturn(coupon);
        given(shoppingcardRepository.save(shoppingcard)).willReturn(shoppingcard);

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                put("/shoppingcard/applyCoupon/" + coupon.getName())
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON).content(
                        jsonShoppingCard.write(shoppingcard).getJson()))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertEquals(response.getContentAsString(), localeMessageProvider.getMessage("success.shoppingCard.applyCoupon"));
    }

    @Test
    public void applyDiscounts() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("food");

        Product product = new Product();
        product.setId(1L);
        product.setTitle("apple");
        product.setCategory(category);
        product.setPrice(10.0);

        Map<Product, Long> products = new HashMap<>();
        products.put(product, 3L);
        Set<Category> categorySet = new HashSet<>();
        categorySet.add(category);

        Campaign campaign = new Campaign();
        campaign.setId(1L);
        campaign.setPurchasedItem(2.0);
        campaign.setDiscountType(DiscountType.RATE);
        campaign.setDiscount(10.0);
        campaign.setCategories(categorySet);

        ShoppingCard shoppingcard = new ShoppingCard();
        shoppingcard.setId(1L);
        shoppingcard.setTotalPriceWithCoupon(30.0);
        shoppingcard.setTotalPriceWithCampaign(30.0);
        shoppingcard.setTotalPriceWithoutDiscount(30.0);
        shoppingcard.setProducts(products);

        // given
        given(campaignService.get(campaign.getId())).willReturn(campaign);
        given(shoppingcardRepository.save(shoppingcard)).willReturn(shoppingcard);

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                put("/shoppingcard/applyDiscounts")
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON).content(
                        jsonShoppingCard.write(shoppingcard).getJson()))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertEquals(response.getContentAsString(), localeMessageProvider.getMessage("success.shoppingCard.applyDiscounts"));
    }

    @Test
    public void getCouponDiscount() throws Exception {
        ShoppingCard shoppingcard = new ShoppingCard();
        shoppingcard.setId(1L);
        shoppingcard.setTotalPriceWithCoupon(20.0);
        shoppingcard.setTotalPriceWithCampaign(25.0);
        shoppingcard.setTotalPriceWithoutDiscount(30.0);

        // given
        given(shoppingcardRepository.findById(shoppingcard.getId())).willReturn(Optional.of(shoppingcard));

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                get("/shoppingcard/getCouponDiscount")
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON).content(
                        jsonShoppingCard.write(shoppingcard).getJson()
                )).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void getCampaignDiscount() throws Exception {
        ShoppingCard shoppingcard = new ShoppingCard();
        shoppingcard.setId(1L);
        shoppingcard.setTotalPriceWithCoupon(20.0);
        shoppingcard.setTotalPriceWithCampaign(25.0);
        shoppingcard.setTotalPriceWithoutDiscount(30.0);

        // given
        given(shoppingcardRepository.findById(shoppingcard.getId())).willReturn(Optional.of(shoppingcard));

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                get("/shoppingcard/getCampaignDiscount")
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON).content(
                        jsonShoppingCard.write(shoppingcard).getJson()
                )).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void getTotalAmountAfterDiscounts() throws Exception {
        ShoppingCard shoppingcard = new ShoppingCard();
        shoppingcard.setId(1L);
        shoppingcard.setTotalPriceWithCoupon(20.0);
        shoppingcard.setTotalPriceWithCampaign(25.0);
        shoppingcard.setTotalPriceWithoutDiscount(30.0);

        // given
        given(shoppingcardRepository.findById(shoppingcard.getId())).willReturn(Optional.of(shoppingcard));

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                get("/shoppingcard/getTotalAmountAfterDiscounts")
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON).content(
                        jsonShoppingCard.write(shoppingcard).getJson()
                )).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void getDeliveryCost() throws Exception {
        ShoppingCard shoppingcard = new ShoppingCard();
        shoppingcard.setId(1L);
        shoppingcard.setTotalPriceWithCoupon(20.0);
        shoppingcard.setTotalPriceWithCampaign(25.0);
        shoppingcard.setTotalPriceWithoutDiscount(30.0);

        DeliveryCostDTO deliveryCostDTO = new DeliveryCostDTO();
        deliveryCostDTO.setShoppingCardId(shoppingcard.getId());
        deliveryCostDTO.setCostPerDelivery(3.0);
        deliveryCostDTO.setCostPerProduct(4.0);
        deliveryCostDTO.setFixedCost(2.99);

        // given
        given(shoppingcardRepository.findById(shoppingcard.getId())).willReturn(Optional.of(shoppingcard));

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                get("/shoppingcard/getDeliveryCost")
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON).content(
                        jsonDeliveryCost.write(deliveryCostDTO).getJson()
                )).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void print() throws Exception {
        ShoppingCard shoppingcard = new ShoppingCard();
        shoppingcard.setId(1L);
        shoppingcard.setTotalPriceWithCoupon(20.0);
        shoppingcard.setTotalPriceWithCampaign(25.0);
        shoppingcard.setTotalPriceWithoutDiscount(30.0);

        DeliveryCostDTO deliveryCostDTO = new DeliveryCostDTO();
        deliveryCostDTO.setShoppingCardId(shoppingcard.getId());
        deliveryCostDTO.setCostPerDelivery(3.0);
        deliveryCostDTO.setCostPerProduct(4.0);
        deliveryCostDTO.setFixedCost(2.99);

        // given
        given(shoppingcardRepository.findById(shoppingcard.getId())).willReturn(Optional.of(shoppingcard));

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                get("/shoppingcard/print")
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON).content(
                        jsonDeliveryCost.write(deliveryCostDTO).getJson()
                )).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

}
