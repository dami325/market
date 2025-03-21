package io.dami.market.config.scheduler;

import io.dami.market.domain.coupon.Coupon;
import io.dami.market.domain.coupon.CouponRepository;
import io.dami.market.domain.coupon.CouponService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponScheduler {

  private final CouponService couponService;
  private final CouponRepository couponRepository;

  @Scheduled(fixedRate = 5000)
  public void processCoupons() {
    List<Coupon> coupons = couponRepository.findAllAvailableCoupons();
    for (Coupon coupon : coupons) {
      couponService.processCouponIssuance(coupon.getId(),
          coupon.getTotalQuantity() - coupon.getIssuedQuantity());
    }
  }
}
