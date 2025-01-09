package io.dami.market.infra.coupon;

import io.dami.market.domain.coupon.Coupon;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {

    @Query("""
            SELECT c
            FROM Coupon c
            WHERE 1 = 1
                AND c.totalQuantity > c.issuedQuantity
                AND c.endDate > now()
                AND c.id NOT IN (
                SELECT uc.coupon.id
                FROM UserCoupon uc
                WHERE uc.user.id = :userId
            )
    """)
    List<Coupon> getFirstServedCoupons(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT cp
    FROM Coupon cp
    WHERE cp.id = :couponId
    """)
    Optional<Coupon> findByIdWithLock(Long couponId);
}
