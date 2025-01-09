package io.dami.market.domain.coupon;

import java.util.List;

public interface CouponRepository {
    Coupon getCoupon(Long couponId);

    List<Coupon> getFirstServedCoupons(Long userId);
}
