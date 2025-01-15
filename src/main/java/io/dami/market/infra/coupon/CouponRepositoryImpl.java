package io.dami.market.infra.coupon;

import io.dami.market.domain.coupon.Coupon;
import io.dami.market.domain.coupon.CouponRepository;
import io.dami.market.domain.user.UserCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponJpaRepository couponJpaRepository;
    private final UserCouponJpaRepository userCouponJpaRepository;

    @Override
    public List<Coupon> getFirstServedCoupons(Long userId) {
        return couponJpaRepository.getFirstServedCoupons(userId);
    }

    @Override
    public Coupon save(Coupon coupon) {
        return couponJpaRepository.save(coupon);
    }

    @Override
    public Optional<UserCoupon> findUserCoupon(Long userCouponId) {
        if (userCouponId == null) {
            return Optional.empty();
        }
        return userCouponJpaRepository.findById(userCouponId);
    }

    @Override
    public Coupon getCouponWithLock(Long couponId) {
        return couponJpaRepository.findByIdWithLock(couponId)
                .orElseThrow(() -> new IllegalArgumentException("쿠폰을 찾을 수 없음"));
    }
}
