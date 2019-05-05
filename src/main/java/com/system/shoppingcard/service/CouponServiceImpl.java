package com.system.shoppingcard.service;

import com.system.shoppingcard.domain.Coupon;
import com.system.shoppingcard.domain.DiscountType;
import com.system.shoppingcard.dto.CouponDTO;
import com.system.shoppingcard.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final EntityManager entityManager;

    private final static String NO_COUPON_ID = "No such coupon id";
    private final static String COUPON_NOT_FOUND = "Coupon not found";

    @Override
    public void add(CouponDTO couponDTO) {
        Coupon coupon = createCoupon(couponDTO);
        DiscountType discountType = DiscountType.valueOf(couponDTO.getDiscountType());
        coupon.setDiscountType(discountType);
        coupon.setName(couponDTO.getName());
        couponRepository.save(coupon);
    }

    @Override
    public void update(CouponDTO couponDTO, long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NO_COUPON_ID));

        coupon.setMinimumPurchaseAmount(couponDTO.getMinimumPurchaseAmount());
        coupon.setDiscountRate(couponDTO.getDiscountRate());
        DiscountType discountType = DiscountType.valueOf(couponDTO.getDiscountType());
        coupon.setDiscountType(discountType);
        coupon.setName(couponDTO.getName());
        couponRepository.save(coupon);
    }

    @Override
    public void delete(long id) {
        couponRepository.deleteById(id);
    }

    @Override
    public List<Coupon> getAll() {
        return couponRepository.findAll();
    }

    @Override
    public Coupon get(long id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NO_COUPON_ID));
    }

    @Override
    public Coupon getByName(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Coupon> q = cb.createQuery(Coupon.class);
        Root<Coupon> c = q.from(Coupon.class);
        ParameterExpression<String> p = cb.parameter(String.class);
        q.select(c).where(cb.equal(c.get("name"), p));
        TypedQuery<Coupon> query = entityManager.createQuery(q);
        query.setParameter(p, name);
        List<Coupon> results = query.getResultList();
        if (results.size() == 0)
            throw new IllegalArgumentException(COUPON_NOT_FOUND);

        return results.get(0);
    }

    private Coupon createCoupon(CouponDTO couponDTO) {
        Coupon coupon = new Coupon();
        coupon.setMinimumPurchaseAmount(couponDTO.getMinimumPurchaseAmount());
        coupon.setDiscountRate(couponDTO.getDiscountRate());

        return coupon;
    }

}
