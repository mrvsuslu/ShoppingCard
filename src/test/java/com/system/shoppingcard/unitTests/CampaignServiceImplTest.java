package com.system.shoppingcard.unitTests;

import com.system.shoppingcard.domain.Campaign;
import com.system.shoppingcard.domain.Category;
import com.system.shoppingcard.domain.DiscountType;
import com.system.shoppingcard.dto.CampaignDTO;
import com.system.shoppingcard.repository.CampaignRepository;
import com.system.shoppingcard.service.CampaignServiceImpl;
import com.system.shoppingcard.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CampaignServiceImplTest {

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CampaignServiceImpl campaignService;

    @Test
    public void testAdd() {

        ArgumentCaptor<Campaign> campaignCaptor = ArgumentCaptor.forClass(Campaign.class);

        Category category = new Category();
        category.setTitle("food");
        Campaign campaign = new Campaign();
        campaign.setDiscountType(DiscountType.AMOUNT);

        CampaignDTO campaignDTO = new CampaignDTO();
        long categoryId = 2L;
        campaignDTO.setCategoryId(categoryId);
        double discount = 3L;
        campaignDTO.setDiscount(discount);
        double purchasedItem = 4L;
        campaignDTO.setPurchasedItem(purchasedItem);
        campaignDTO.setDiscountType("AMOUNT");

        when(categoryService.get(categoryId)).thenReturn(category);
        when(campaignRepository.findById(any())).thenReturn(Optional.of(campaign));

        campaignService.add(campaignDTO);
        verify(campaignRepository).save(campaignCaptor.capture());
    }

    @Test
    public void testUpdate() {

        ArgumentCaptor<Campaign> campaignCaptor = ArgumentCaptor.forClass(Campaign.class);

        Category category = new Category();
        category.setTitle("food");
        Campaign campaign = new Campaign();
        campaign.setDiscountType(DiscountType.AMOUNT);

        CampaignDTO campaignDTO = new CampaignDTO();
        long categoryId = 2L;
        campaignDTO.setCategoryId(categoryId);
        long campaignId = 1L;
        double discount = 3L;
        campaignDTO.setDiscount(discount);
        double purchasedItem = 4L;
        campaignDTO.setPurchasedItem(purchasedItem);
        campaignDTO.setDiscountType("AMOUNT");

        when(categoryService.get(categoryId)).thenReturn(category);
        when(campaignRepository.findById(any())).thenReturn(Optional.of(campaign));

        campaignService.update(campaignDTO, campaignId);
        verify(campaignRepository).save(campaignCaptor.capture());
    }

    @Test
    public void delete() {

        double discount = 0L;
        double purchasedItem = 2L;
        long campaignId = 0L;
        String discountType = "RATE";
        long categoryId = 1L;

        CampaignDTO campaignDTO = new CampaignDTO();
        campaignDTO.setDiscount(discount);
        campaignDTO.setPurchasedItem(purchasedItem);
        campaignDTO.setDiscountType(discountType);

        Campaign campaign = new Campaign();
        campaign.setId(campaignId);
        Category category = new Category();
        category.setId(categoryId);

        campaign.getCategories().add(category);

        when(categoryService.get(categoryId)).thenReturn(category);
        when(campaignRepository.findById(any())).thenReturn(Optional.of(campaign));

        campaignService.delete(campaignId);
        verify(campaignRepository).deleteById(campaign.getId());
    }

    @Test
    public void getAll() {
        campaignService.getAll();
        verify(campaignRepository).findAll();
    }

    @Test
    public void get() {
        long expectedId = 1L;
        when(campaignRepository.findById(expectedId)).thenReturn(Optional.of(new Campaign()));
        campaignService.get(expectedId);
        verify(campaignRepository).findById(Mockito.eq(expectedId));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getShouldThrowAnException() {
        campaignService.get(555);
    }
}