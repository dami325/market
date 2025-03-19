package io.dami.market.infrastructure.coupon;

import io.dami.market.domain.coupon.IssuedCoupon;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IssuedCouponJpaRepository extends JpaRepository<IssuedCoupon, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select issued
            from IssuedCoupon issued
            where issued.id = :issuedCouponId
            """)
    Optional<IssuedCoupon> findByIdWithLock(Long issuedCouponId);
}
