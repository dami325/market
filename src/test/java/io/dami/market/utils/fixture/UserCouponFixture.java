package io.dami.market.utils.fixture;

import io.dami.market.domain.coupon.Coupon;
import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserCoupon;

import java.time.LocalDateTime;

public record UserCouponFixture() {

    public static UserCoupon userCoupon(Coupon coupon, User user) {
        return UserCoupon.builder()
                .coupon(coupon)
                .user(user)
                .issuedAt(LocalDateTime.now())
                .build();
    }
}
