package io.dami.market.interfaces.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UserResponse() {
    public record UserCoupon(
            Long couponId,
            String name,
            BigDecimal discountAmount,
            LocalDateTime startDate,
            LocalDateTime endDate,
            LocalDateTime issuedAt
    ) {
    }
}
