package com.system.shoppingcard.service;

import com.system.shoppingcard.domain.Campaign;
import com.system.shoppingcard.domain.Category;
import com.system.shoppingcard.domain.DiscountType;
import com.system.shoppingcard.dto.CampaignDTO;
import com.system.shoppingcard.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepository campaignRepository;
    private final CategoryService categoryService;

    private static final String NO_CAMPAIGN_ID = "No such campaign id";

    @Override
    public void add(CampaignDTO campaignDTO) {
        Campaign campaign = createCampaign(campaignDTO);
        Category category = categoryService.get(campaignDTO.getCategoryId());
        campaign.getCategories().add(category);
        DiscountType discountType = DiscountType.valueOf(campaignDTO.getDiscountType());
        campaign.setDiscountType(discountType);

        campaignRepository.save(campaign);
    }

    @Override
    public void update(CampaignDTO campaignDTO, long id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NO_CAMPAIGN_ID));

        Category categoryFromDb = categoryService.get(campaignDTO.getCategoryId());
        Category category = campaign.getCategories().stream()
                .filter(c -> c.equals(categoryFromDb))
                .findAny()
                .orElse(null);
        if (category == null)
            campaign.getCategories().add(categoryFromDb);
        DiscountType discountType = DiscountType.valueOf(campaignDTO.getDiscountType());
        campaign.setDiscountType(discountType);
        campaign.setPurchasedItem(campaignDTO.getPurchasedItem());
        campaign.setDiscount(campaignDTO.getDiscount());
        campaignRepository.save(campaign);
    }

    @Override
    public void delete(long id) {
        campaignRepository.deleteById(id);
    }

    @Override
    public List<Campaign> getAll() {
        return campaignRepository.findAll();
    }

    @Override
    public Campaign get(long id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NO_CAMPAIGN_ID));
    }

    @Override
    public List<Campaign> getAllByCategories(List<Long> categories) {
        List<Campaign> campaignList = new ArrayList<>();
        for (Campaign campaign : campaignRepository.findAll()) {
            Optional<Category> related = campaign.getCategories().stream()
                    .filter(e -> categories.contains(e.getId()))
                    .findAny();

            if (related.isPresent() && !campaignList.contains(campaign))
                campaignList.add(campaign);
        }

        if (campaignList.size() == 0)
            throw new IllegalArgumentException("Campaign not found");

        return campaignList;
    }

    private Campaign createCampaign(CampaignDTO campaignDTO) {
        Campaign campaign = new Campaign();
        campaign.setDiscount(campaignDTO.getDiscount());
        campaign.setPurchasedItem(campaignDTO.getPurchasedItem());

        return campaign;
    }

}
