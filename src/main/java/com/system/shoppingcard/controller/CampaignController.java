package com.system.shoppingcard.controller;

import com.system.shoppingcard.authentication.ShoppingCardAuthService;
import com.system.shoppingcard.domain.Campaign;
import com.system.shoppingcard.dto.CampaignDTO;
import com.system.shoppingcard.messageProvider.LocaleMessageProvider;
import com.system.shoppingcard.service.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campaign")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;
    private final ShoppingCardAuthService shoppingCardAuthService;

    private final LocaleMessageProvider localeMessageProvider;

    @PostMapping(value = "/add")
    @ResponseBody
    public ResponseEntity addCampaign(@RequestHeader HttpHeaders httpHeaders, @RequestBody CampaignDTO campaign) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        campaignService.add(campaign);
        return new ResponseEntity<>(localeMessageProvider.getMessage("success.campaign.add"), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}")
    @ResponseBody
    public ResponseEntity updateCampaign(@RequestHeader HttpHeaders httpHeaders, @RequestBody CampaignDTO campaign, @PathVariable("id") final long id) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        campaignService.update(campaign, id);
        return new ResponseEntity<>(localeMessageProvider.getMessage("success.campaign.update"), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    @ResponseBody
    public ResponseEntity deleteCampaign(@RequestHeader HttpHeaders httpHeaders, @PathVariable("id") final long id) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        campaignService.delete(id);
        return new ResponseEntity<>(localeMessageProvider.getMessage("success.campaign.delete"), HttpStatus.OK);
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Campaign>> getAllCategories(@RequestHeader HttpHeaders httpHeaders) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        return new ResponseEntity<>(campaignService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<Campaign> getCampaign(@RequestHeader HttpHeaders httpHeaders, @PathVariable("id") final long id) {
        shoppingCardAuthService.checkHeaderAuthToken(httpHeaders);
        return new ResponseEntity<>(campaignService.get(id), HttpStatus.OK);
    }

}
