package io.dami.market.domain.coupon;

import io.dami.market.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    @Transactional(readOnly = true)
    public List<Coupon> getFirstServedCoupons(Long userId) {
        return couponRepository.getFirstServedCoupons(userId);
    }

    @Transactional
    public void issueACoupon(Long couponId, Long userId) {
        Coupon coupon = couponRepository.getCouponWithLock(couponId);
        coupon.issuedCoupon(userId);
    }

    @Transactional(readOnly = true)
    public BigDecimal useCoupon(Long issuedCouponId) {
        return couponRepository.findIssuedCoupon(issuedCouponId)
                .map(IssuedCoupon::useCoupon)
                .orElse(BigDecimal.ZERO);
    }
}
