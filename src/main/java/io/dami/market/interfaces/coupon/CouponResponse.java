package io.dami.market.interfaces.coupon;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponResponse() {


    public record CouponDetails(
            Long couponId,
            String name,
            BigDecimal discountAmount,
            Integer totalQuantity,
            Integer issuedQuantity,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {

    }
}
