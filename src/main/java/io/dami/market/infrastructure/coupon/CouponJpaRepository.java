package io.dami.market.infrastructure.coupon;

import io.dami.market.domain.coupon.Coupon;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {

  @Query("""
      SELECT c
      FROM Coupon c
      WHERE 1 = 1
      AND c.totalQuantity > c.issuedQuantity
      AND c.endDate > now()
      AND c.id NOT IN (
      SELECT uc.coupon.id
      FROM IssuedCoupon uc
      WHERE uc.userId = :userId
      )
      """)
  List<Coupon> getFirstServedCoupons(Long userId);

  @Query("""
      SELECT c
      FROM Coupon c
      INNER JOIN c.issuedCoupons issued
      WHERE issued.userId = :userId
      """)
  List<Coupon> getCouponsByUserId(Long userId);

  @Query("""
        SELECT c
        FROM Coupon c
        WHERE c.issuedQuantity < c.totalQuantity
        AND c.endDate > now()
      """)
  List<Coupon> findAllAvailableCoupons();

  @Query("""
      SELECT c
      FROM Coupon c
      WHERE c.id = :couponId
      """)
  @Lock(value = LockModeType.PESSIMISTIC_WRITE)
  Optional<Coupon> findByIdWithLock(Long couponId);
}
