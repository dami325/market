package io.dami.market.infra.coupon;

import io.dami.market.domain.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {

    @Query("""
            SELECT c
            FROM Coupon c
            WHERE 1 = 1
                AND c.totalQuantity < c.issuedQuantity
                AND c.endDate > now()
                AND c.id NOT IN (
                SELECT uc.coupon.id
                FROM UserCoupon uc
                WHERE uc.user.id = :userId
            )
    """)
    List<Coupon> getFirstServedCoupons(Long userId);
}
