package io.dami.market.interfaces.coupon;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.dami.market.domain.coupon.Coupon;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponResponse() {


    public record CouponDetails(
            @Schema(example = "5")
            Long couponId,
            @Schema(example = "새해 맞이 쿠폰")
            String name,
            @Schema(example = "1000")
            BigDecimal discountAmount,
            @Schema(example = "30")
            Integer totalQuantity,
            @Schema(example = "15")
            Integer issuedQuantity,
            @Schema(example = "2025-01-10 11:30")
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
            LocalDateTime endDate
    ) {
        public CouponDetails(Coupon coupon) {
            this(
                    coupon.getId(),
                    coupon.getName(),
                    coupon.getDiscountAmount(),
                    coupon.getTotalQuantity(),
                    coupon.getIssuedQuantity(),
                    coupon.getEndDate()
            );
        }
    }
}
