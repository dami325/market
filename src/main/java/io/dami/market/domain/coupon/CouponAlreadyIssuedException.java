package io.dami.market.domain.coupon;

public class CouponAlreadyIssuedException extends RuntimeException {

  public CouponAlreadyIssuedException() {
    super("이미 발급받은 쿠폰입니다.");
  }
}
