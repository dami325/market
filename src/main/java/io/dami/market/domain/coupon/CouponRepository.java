package io.dami.market.domain.coupon;

import java.util.List;
import java.util.Optional;

public interface CouponRepository {

  Coupon getCoupon(Long couponId);

  List<Coupon> getFirstServedCoupons(Long userId);

  Coupon save(Coupon coupon);

  Optional<IssuedCoupon> findIssuedCouponWithLock(Long issuedCouponId);

  List<Coupon> getCouponsByUserId(Long userId);

  List<Coupon> findAllAvailableCoupons();

  Coupon getCouponWithLock(Long couponId);
}
