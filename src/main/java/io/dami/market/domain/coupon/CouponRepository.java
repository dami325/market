package io.dami.market.domain.coupon;

import java.util.List;

public interface CouponRepository {
    Coupon getCouponWithLock(Long couponId);

    List<Coupon> getFirstServedCoupons(Long userId);

    Coupon save(Coupon coupon);
}
