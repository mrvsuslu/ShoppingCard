package com.system.shoppingcard.integrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.shoppingcard.domain.Campaign;
import com.system.shoppingcard.domain.Category;
import com.system.shoppingcard.domain.DiscountType;
import com.system.shoppingcard.messageProvider.LocaleMessageProvider;
import com.system.shoppingcard.repository.CampaignRepository;
import com.system.shoppingcard.service.CategoryService;
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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CampaignControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CampaignRepository campaignRepository;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private LocaleMessageProvider localeMessageProvider;

    private JacksonTester<Campaign> jsonCampaign;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void createCampaign() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("food");

        Set<Category> categorySet = new HashSet<>();
        categorySet.add(category);

        Campaign campaign = new Campaign();
        campaign.setId(1L);
        campaign.setPurchasedItem(2.0);
        campaign.setDiscountType(DiscountType.RATE);
        campaign.setDiscount(10.0);
        campaign.setCategories(categorySet);

        // given
        given(categoryService.get(category.getId())).willReturn(category);
        given(campaignRepository.save(campaign)).willReturn(campaign);

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                post("/campaign/add")
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON).content(
                        jsonCampaign.write(campaign).getJson()
                )).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertEquals(response.getContentAsString(), localeMessageProvider.getMessage("success.campaign.add"));
    }


    @Test
    public void updateCampaign() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("food");

        Set<Category> categorySet = new HashSet<>();
        categorySet.add(category);

        Campaign campaign = new Campaign();
        campaign.setId(1L);
        campaign.setPurchasedItem(2.0);
        campaign.setDiscountType(DiscountType.RATE);
        campaign.setDiscount(10.0);
        campaign.setCategories(categorySet);

        // given
        given(categoryService.get(category.getId())).willReturn(category);
        given(campaignRepository.findById(campaign.getId())).willReturn(Optional.of(campaign));

        Campaign newCampaign = new Campaign();
        newCampaign.setId(1L);
        newCampaign.setPurchasedItem(2.0);
        newCampaign.setDiscountType(DiscountType.RATE);
        newCampaign.setDiscount(12.0);
        newCampaign.setCategories(categorySet);

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                put("/campaign/update/" + campaign.getId())
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON).content(
                        jsonCampaign.write(newCampaign).getJson()
                )).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertEquals(response.getContentAsString(), localeMessageProvider.getMessage("success.campaign.update"));
    }

    @Test
    public void getAllCategories() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("food");

        Set<Category> categorySet = new HashSet<>();
        categorySet.add(category);

        Campaign campaign = new Campaign();
        campaign.setId(1L);
        campaign.setPurchasedItem(2.0);
        campaign.setDiscountType(DiscountType.RATE);
        campaign.setDiscount(10.0);
        campaign.setCategories(categorySet);

        // given
        given(campaignRepository.save(campaign)).willReturn(campaign);

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                get("/campaign/getAll")
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void getCampaign() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("food");

        Set<Category> categorySet = new HashSet<>();
        categorySet.add(category);

        Campaign campaign = new Campaign();
        campaign.setId(1L);
        campaign.setPurchasedItem(2.0);
        campaign.setDiscountType(DiscountType.RATE);
        campaign.setDiscount(10.0);
        campaign.setCategories(categorySet);

        // given
        given(campaignRepository.findById(campaign.getId())).willReturn(Optional.of(campaign));

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                get("/campaign/get/" + campaign.getId())
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    public void deleteCampaign() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("food");

        Set<Category> categorySet = new HashSet<>();
        categorySet.add(category);

        Campaign campaign = new Campaign();
        campaign.setId(1L);
        campaign.setPurchasedItem(2.0);
        campaign.setDiscountType(DiscountType.RATE);
        campaign.setDiscount(10.0);
        campaign.setCategories(categorySet);

        // given
        given(campaignRepository.save(campaign)).willReturn(campaign);

        // when
        MockHttpServletResponse response = this.mockMvc.perform(
                delete("/campaign/delete/" + campaign.getId())
                        .header(HttpHeaders.WWW_AUTHENTICATE, localeMessageProvider.getMessage("authentication.token"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertEquals(response.getContentAsString(), localeMessageProvider.getMessage("success.campaign.delete"));
    }
}