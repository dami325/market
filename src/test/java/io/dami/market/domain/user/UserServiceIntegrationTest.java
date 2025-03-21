package io.dami.market.domain.user;

import io.dami.market.domain.coupon.Coupon;
import io.dami.market.domain.coupon.CouponRepository;
import io.dami.market.utils.IntegrationServiceTest;
import io.dami.market.utils.fixture.CouponFixture;
import io.dami.market.utils.fixture.UserFixture;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class UserServiceIntegrationTest extends IntegrationServiceTest {

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CouponRepository couponRepository;

  @Test
  @Transactional
  void 보유_쿠폰_조회_성공() {
    // given
    User user = userRepository.save(UserFixture.user("박주닮"));
    Coupon couponA = couponRepository.save(CouponFixture.coupon("설날쿠폰"));
    Coupon couponB = couponRepository.save(CouponFixture.coupon("새해쿠폰"));
    couponA.issuedCoupon(user.getId());
    couponB.issuedCoupon(user.getId());

    // when
    List<Coupon> result = userService.getUserCoupons(user.getId());

    // then
    Assertions.assertThat(result.size()).isEqualTo(2);
  }
}
