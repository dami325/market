package io.dami.market.utils.fixture;

import io.dami.market.domain.coupon.Coupon;
import io.dami.market.domain.user.User;
import io.dami.market.domain.coupon.IssuedCoupon;

import java.time.LocalDateTime;

public record UserCouponFixture() {

    public static IssuedCoupon userCoupon(Coupon coupon, Long userId) {
        return IssuedCoupon.builder()
                .coupon(coupon)
                .userId(userId)
                .issuedAt(LocalDateTime.now())
                .build();
    }
}
