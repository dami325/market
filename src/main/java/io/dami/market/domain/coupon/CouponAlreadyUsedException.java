package io.dami.market.domain.coupon;

public class CouponAlreadyUsedException extends RuntimeException {

  public CouponAlreadyUsedException(String message) {
    super(message);
  }
}
