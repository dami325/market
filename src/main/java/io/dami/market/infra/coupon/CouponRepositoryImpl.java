package io.dami.market.infra.coupon;

import io.dami.market.domain.coupon.Coupon;
import io.dami.market.domain.coupon.CouponRepository;
import io.dami.market.domain.coupon.IssuedCoupon;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponJpaRepository couponJpaRepository;
    private final IssuedCouponJpaRepository issuedCouponJpaRepository;

    @Override
    public List<Coupon> getFirstServedCoupons(Long userId) {
        return couponJpaRepository.getFirstServedCoupons(userId);
    }

    @Override
    public Coupon save(Coupon coupon) {
        return couponJpaRepository.save(coupon);
    }

    @Override
    public Optional<IssuedCoupon> findIssuedCoupon(Long issuedCouponId) {
        if (issuedCouponId == null) {
            return Optional.empty();
        }
        return issuedCouponJpaRepository.findById(issuedCouponId);
    }

    @Override
    public List<Coupon> getCouponsByUserId(Long userId) {
        return couponJpaRepository.getCouponsByUserId(userId);
    }

    @Override
    public Coupon getCouponWithLock(Long couponId) {
        return couponJpaRepository.findByIdWithLock(couponId)
                .orElseThrow(() -> new EntityNotFoundException("쿠폰을 찾을 수 없음"));
    }
}
