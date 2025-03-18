package io.dami.market.domain.coupon;

import io.dami.market.infra.redis.redisson.DistributedLock;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {

  private final CouponRepository couponRepository;
  private final RedisTemplate<String, String> redisTemplate;
  private static final String COUPON_REQUEST_KEY = "coupon-%d-requests";
  private static final String COUPON_ISSUED_KEY = "coupon-%d-issued";

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

  public void issueACouponRedis(Long couponId, Long userId) {
    String issuedKey = String.format(COUPON_ISSUED_KEY, couponId);
    if (Boolean.TRUE.equals(
        redisTemplate.opsForSet().isMember(issuedKey, String.valueOf(userId)))) {
      throw new CouponAlreadyIssuedException();
    }
    String requestKey = String.format(COUPON_REQUEST_KEY, couponId);
    Long timestamp = System.currentTimeMillis();
    redisTemplate.opsForZSet().add(requestKey, userId.toString(), timestamp);
  }

  @Transactional
  public void processCouponIssuance(Long couponId, int availableCoupons) {
    String requestKey = String.format(COUPON_REQUEST_KEY, couponId);
    String issuedKey = String.format(COUPON_ISSUED_KEY, couponId);

    Set<String> selectedUsers = redisTemplate.opsForZSet().popMin(requestKey, availableCoupons)
        .stream().map(TypedTuple::getValue)
        .collect(Collectors.toSet());

    for (String userId : selectedUsers) {

      if (!Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(issuedKey, userId))) {
        log.info("쿠폰 발급 실행: userId = {}", userId);
        Coupon coupon = couponRepository.getCoupon(couponId);
        coupon.issuedCoupon(Long.valueOf(userId));
        redisTemplate.opsForSet().add(issuedKey, userId);
      }
    }

    Set<String> failedUsers = redisTemplate.opsForZSet().range(requestKey, 0, -1);

    for (String failedUser : failedUsers) {
      sendFailureMessage(Long.valueOf(failedUser));
    }
    redisTemplate.delete(requestKey);
  }

  private void sendFailureMessage(Long userId) {
    log.info("쿠폰 발급 실패: userId = {}", userId);
  }
}
