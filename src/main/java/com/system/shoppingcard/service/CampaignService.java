package com.system.shoppingcard.service;

import com.system.shoppingcard.domain.Campaign;
import com.system.shoppingcard.dto.CampaignDTO;

import java.util.List;

public interface CampaignService extends DiscountService<Campaign, CampaignDTO> {

    List<Campaign> getAllByCategories(List<Long> categories);

}
