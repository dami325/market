package io.dami.market.interfaces.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.dami.market.domain.coupon.Coupon;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UserResponse() {

  public record UserCouponDetails(
      @Schema(example = "5")
      Long couponId,
      @Schema(example = "새해 맞이 쿠폰")
      String name,
      @Schema(example = "3000")
      BigDecimal discountAmount,
      @Schema(example = "2025-01-10 11:30")
      @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
      LocalDateTime endDate,
      @Schema(example = "2025-01-07 11:30")
      @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
      LocalDateTime issuedAt
  ) {

    public UserCouponDetails(Coupon coupon) {
      this(coupon.getId(), coupon.getName(), coupon.getDiscountAmount(), coupon.getEndDate(),
          coupon.getCreatedAt());
    }
  }
}
