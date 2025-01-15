package io.dami.market.infra.coupon;

import io.dami.market.domain.user.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponJpaRepository extends JpaRepository<UserCoupon, Long> {

}
