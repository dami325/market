package io.dami.market.domain.coupon;

import java.util.List;
import java.util.Optional;

public interface CouponRepository {
    Coupon getCouponWithLock(Long couponId);

    List<Coupon> getFirstServedCoupons(Long userId);

    Coupon save(Coupon coupon);

    Optional<IssuedCoupon> findIssuedCoupon(Long issuedCouponId);

    List<Coupon> getCouponsByUserId(Long userId);
}
