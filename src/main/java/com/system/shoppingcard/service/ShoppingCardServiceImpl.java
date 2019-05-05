package com.system.shoppingcard.service;

import com.system.shoppingcard.calculator.Calculator;
import com.system.shoppingcard.calculator.CalculatorImpl;
import com.system.shoppingcard.domain.Campaign;
import com.system.shoppingcard.domain.Coupon;
import com.system.shoppingcard.domain.Product;
import com.system.shoppingcard.domain.ShoppingCard;
import com.system.shoppingcard.dto.DeliveryCostDTO;
import com.system.shoppingcard.dto.ProductResultDTO;
import com.system.shoppingcard.dto.ShoppingCardDTO;
import com.system.shoppingcard.dto.ShoppingResultDTO;
import com.system.shoppingcard.repository.ShoppingCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShoppingCardServiceImpl implements ShoppingCardService {

    private final ShoppingCardRepository shoppingCardRepository;
    private final ProductService productService;
    private final CouponService couponService;
    private final CampaignService campaignService;

    private final Calculator calculator = new CalculatorImpl();

    private final static String NO_CART = "No such cart";
    private final static String COUPON_CAMPAIGN_RELATION = "Coupons can not be apply before the campaigns.";
    private final static String COUPON_APPLIED = "Coupon already applied.";
    private final static String NO_PRODUCT = "No such product";
    private final static String QUANTITY_RELATION = "Product quantity can not be more than the quantity on the cart";

    @Override
    public void add() {
        ShoppingCard shoppingCard = new ShoppingCard();
        shoppingCardRepository.save(shoppingCard);
    }

    @Override
    public void addItem(ShoppingCardDTO shoppingCardDTO) {
        Product product = productService.get(shoppingCardDTO.getProductId());

        ShoppingCard shoppingCard = shoppingCardRepository.findById(shoppingCardDTO.getShoppingCardId())
                .orElseThrow(() -> new IllegalArgumentException(NO_PRODUCT));

        shoppingCard.getProducts().put(product, shoppingCardDTO.getQuantity());
        shoppingCard.setTotalPriceWithoutDiscount(shoppingCard.getTotalPriceWithoutDiscount() +
                (product.getPrice() * shoppingCardDTO.getQuantity()));

        shoppingCardRepository.save(shoppingCard);
    }

    @Override
    public void removeItem(ShoppingCardDTO shoppingCardDTO) {
        ShoppingCard shoppingCard = shoppingCardRepository.findById(shoppingCardDTO.getShoppingCardId())
                .orElseThrow(() -> new IllegalArgumentException(NO_PRODUCT));

        Product product = productService.get(shoppingCardDTO.getProductId());

        if (shoppingCard.getProducts().get(product) < shoppingCardDTO.getQuantity())
            throw new IllegalArgumentException(QUANTITY_RELATION);
        else if (shoppingCard.getProducts().get(product) == shoppingCardDTO.getQuantity())
            shoppingCardRepository.deleteById(shoppingCard.getId());

        shoppingCard.getProducts().put(product,
                shoppingCard.getProducts().get(product) - shoppingCardDTO.getQuantity());
        shoppingCardRepository.save(shoppingCard);
    }

    @Override
    public void removeAll() {
        shoppingCardRepository.deleteAll();
    }

    @Override
    public List<ShoppingCard> getAll() {
        return shoppingCardRepository.findAll();
    }

    @Override
    public ShoppingCard get(long id) {
        return shoppingCardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NO_CART));
    }

    @Override
    public void applyCoupon(ShoppingCardDTO shoppingCardDTO, String couponName) {
        ShoppingCard shoppingCard = shoppingCardRepository.findById(shoppingCardDTO.getShoppingCardId())
                .orElseThrow(() -> new IllegalArgumentException(NO_CART));

        Coupon coupon = couponService.getByName(couponName);
        if (shoppingCard.getCoupons().contains(coupon))
            throw new IllegalArgumentException(COUPON_APPLIED);

        if (shoppingCard.isAppliedDiscounts()) {
            shoppingCard = calculator.calculateCouponDiscount(shoppingCard, coupon);
        } else
            throw new IllegalArgumentException(COUPON_CAMPAIGN_RELATION);

        shoppingCard = calculator.calculateCouponDiscount(shoppingCard, coupon);
        shoppingCardRepository.save(shoppingCard);
    }

    @Override
    public void applyDiscounts(ShoppingCardDTO shoppingCardDTO) {
        ShoppingCard shoppingCard = shoppingCardRepository.findById(shoppingCardDTO.getShoppingCardId())
                .orElseThrow(() -> new IllegalArgumentException(NO_CART));

        List<Long> categories = new ArrayList<>();
        for (Map.Entry<Product, Long> item : shoppingCard.getProducts().entrySet()) {
            if (!categories.contains(item.getKey().getCategory().getId()))
                categories.add(item.getKey().getCategory().getId());
        }
        List<Campaign> campaigns = campaignService.getAllByCategories(categories);

        shoppingCard = calculator.calculateCampaignDiscount(shoppingCard, campaigns);
        shoppingCardRepository.save(shoppingCard);

    }

    @Override
    public double getCouponDiscount(ShoppingCardDTO shoppingCardDTO) {
        ShoppingCard shoppingCard = shoppingCardRepository.findById(shoppingCardDTO.getShoppingCardId())
                .orElseThrow(() -> new IllegalArgumentException(NO_CART));

        return calculator.getCouponDiscount(shoppingCard);
    }

    @Override
    public double getCampaignDiscount(ShoppingCardDTO shoppingCardDTO) {
        ShoppingCard shoppingCard = shoppingCardRepository.findById(shoppingCardDTO.getShoppingCardId())
                .orElseThrow(() -> new IllegalArgumentException(NO_CART));

        return calculator.getCampaignDiscount(shoppingCard);
    }

    @Override
    public double getTotalAmountAfterDiscounts(ShoppingCardDTO shoppingCardDTO) {
        ShoppingCard shoppingCard = shoppingCardRepository.findById(shoppingCardDTO.getShoppingCardId())
                .orElseThrow(() -> new IllegalArgumentException(NO_CART));

        return calculator.getTotalAmountAfterDiscounts(shoppingCard);
    }

    @Override
    public double getDeliveryCost(DeliveryCostDTO deliveryCostDTO) {
        ShoppingCard shoppingCard = shoppingCardRepository.findById(deliveryCostDTO.getShoppingCardId())
                .orElseThrow(() -> new IllegalArgumentException(NO_CART));

        int numberOfDeliveries = findDistinctCategories(shoppingCard);

        return calculator.getDeliveryCost(shoppingCard, deliveryCostDTO, numberOfDeliveries);
    }

    @Override
    public ShoppingResultDTO print(DeliveryCostDTO deliveryCostDTO) {
        ShoppingResultDTO shoppingResultDTO = new ShoppingResultDTO();

        ShoppingCard shoppingCard = shoppingCardRepository.findById(deliveryCostDTO.getShoppingCardId())
                .orElseThrow(() -> new IllegalArgumentException(NO_CART));

        Map<Long, ProductResultDTO> productsMap = new HashMap<>();
        for (Map.Entry<Product, Long> item : shoppingCard.getProducts().entrySet()) {
            ProductResultDTO productResultDTO = new ProductResultDTO();
            productResultDTO.setCategoryName(item.getKey().getCategory().getTitle());
            productResultDTO.setProductName(item.getKey().getTitle());
            productResultDTO.setQuantity(item.getValue());
            productResultDTO.setUnitPrice(item.getKey().getPrice() * item.getValue());

            productsMap.put(item.getKey().getCategory().getId(), productResultDTO);
        }

        productsMap = productsMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        shoppingResultDTO.setProducts(new ArrayList<>(productsMap.values()));
        shoppingResultDTO.setTotalAmount(shoppingCard.getTotalPriceWithCoupon());
        shoppingResultDTO.setTotalPrice(shoppingCard.getTotalPriceWithoutDiscount());
        shoppingResultDTO.setTotalDiscount(shoppingCard.getTotalPriceWithoutDiscount() - shoppingCard.getTotalPriceWithCoupon());
        shoppingResultDTO.setDeliveryCost(getDeliveryCost(deliveryCostDTO));

        return shoppingResultDTO;
    }

    private int findDistinctCategories(ShoppingCard shoppingCard) {
        List<Long> distinctCategories = new ArrayList<>();
        for (Map.Entry<Product, Long> item : shoppingCard.getProducts().entrySet()) {
            if (!distinctCategories.contains(item.getKey().getCategory().getId()))
                distinctCategories.add(item.getKey().getCategory().getId());
        }

        return distinctCategories.size();
    }

}
