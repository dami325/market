package io.dami.market.infra.coupon;

import io.dami.market.domain.coupon.IssuedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssuedCouponJpaRepository extends JpaRepository<IssuedCoupon, Long> {

}
