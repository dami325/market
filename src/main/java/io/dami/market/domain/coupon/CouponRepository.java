package io.dami.market.domain.coupon;

import io.dami.market.domain.user.UserCoupon;

import java.util.List;
import java.util.Optional;

public interface CouponRepository {
    Coupon getCouponWithLock(Long couponId);

    List<Coupon> getFirstServedCoupons(Long userId);

    Coupon save(Coupon coupon);

    Optional<UserCoupon> findUserCoupon(Long userCouponId);
}
