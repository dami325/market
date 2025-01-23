package io.dami.market.domain.coupon;

import io.dami.market.infra.redis.redisson.DistributedLock;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {

  private final CouponRepository couponRepository;

  @Transactional(readOnly = true)
  public List<Coupon> getFirstServedCoupons(Long userId) {
    return couponRepository.getFirstServedCoupons(userId);
  }

  @DistributedLock(key = "'COUPON_' + #couponId")
  @Transactional
  public void issueACoupon(Long couponId, Long userId) {
    Coupon coupon = couponRepository.getCoupon(couponId);
    coupon.issuedCoupon(userId);
  }

  @Transactional(readOnly = true)
  public BigDecimal useCoupon(Long issuedCouponId) {
    return couponRepository.findIssuedCouponWithLock(issuedCouponId)
        .map(IssuedCoupon::useCoupon)
        .orElse(BigDecimal.ZERO);
  }
}
