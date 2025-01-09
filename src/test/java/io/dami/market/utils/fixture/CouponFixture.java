package io.dami.market.utils.fixture;

import io.dami.market.domain.coupon.Coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponFixture() {

    public static Coupon coupon(String name){
        return Coupon.builder()
                .name(name)
                .discountAmount(BigDecimal.valueOf(1000))
                .totalQuantity(10)
                .issuedQuantity(0)
                .endDate(LocalDateTime.now().plusDays(5))
                .build();
    }

    public static Coupon coupon(Long id, String name,BigDecimal discountAmount){
        return Coupon.builder()
                .id(id)
                .name(name)
                .discountAmount(discountAmount)
                .totalQuantity(10)
                .issuedQuantity(0)
                .endDate(LocalDateTime.now().plusDays(5))
                .build();
    }

    public static Coupon coupon(String name,LocalDateTime endDate){
        return Coupon.builder()
                .name(name)
                .discountAmount(BigDecimal.valueOf(1000))
                .totalQuantity(10)
                .issuedQuantity(0)
                .endDate(endDate)
                .build();
    }

    public static Coupon coupon(String name, int totalQuantity, int issuedQuantity){
        return Coupon.builder()
                .name(name)
                .discountAmount(BigDecimal.valueOf(1000))
                .totalQuantity(totalQuantity)
                .issuedQuantity(issuedQuantity)
                .endDate(LocalDateTime.now().plusDays(5))
                .build();
    }

}
