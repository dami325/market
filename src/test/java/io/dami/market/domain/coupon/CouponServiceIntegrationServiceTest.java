package io.dami.market.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;

import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserRepository;
import io.dami.market.utils.IntegrationServiceTest;
import io.dami.market.utils.fixture.CouponFixture;
import io.dami.market.utils.fixture.UserFixture;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class CouponServiceIntegrationServiceTest extends IntegrationServiceTest {

  @Autowired
  private CouponService couponService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CouponRepository couponRepository;

  @Test
  void 선착순_발급_가능_쿠폰_조회_성공() {
    // given
    User user = userRepository.save(UserFixture.user("박주닮"));
    couponRepository.save(CouponFixture.coupon("새해쿠폰"));
    couponRepository.save(CouponFixture.coupon("설날쿠폰"));

    // when
    List<Coupon> result = couponService.getFirstServedCoupons(user.getId());

    // then
    assertThat(result.size()).isEqualTo(2);
  }

  @Test
  @Transactional
  void 선착순_발급_가능_쿠폰_조회_성공2_둘중하나는발급() {
    // given
    User user = userRepository.save(UserFixture.user("박주닮"));
    Coupon couponA = couponRepository.save(CouponFixture.coupon("새해쿠폰"));
    couponRepository.save(CouponFixture.coupon("설날쿠폰"));
    couponA.issuedCoupon(user.getId());

    // when
    List<Coupon> result = couponService.getFirstServedCoupons(user.getId());

    // then
    assertThat(result.size()).isEqualTo(1);
  }

  @Test
  void 선착순_쿠폰_발급_성공() {
    // given
    User user = userRepository.save(UserFixture.user("박주닮"));
    Coupon coupon = couponRepository.save(CouponFixture.coupon("새해쿠폰"));

    // when
    couponService.issueACoupon(coupon.getId(), user.getId());

    // then
    List<Coupon> result = couponRepository.getCouponsByUserId(user.getId());
    Assertions.assertThat(result.size()).isEqualTo(1);
  }

  @Test
  void 선착순_쿠폰_발급_중복요청시_1개만발급_성공() throws InterruptedException {
    // given
    User user = userRepository.save(UserFixture.user("박주닮"));
    Coupon coupon = couponRepository.save(CouponFixture.coupon("새해쿠폰"));
    int threads = 3;
    ExecutorService executorService = Executors.newFixedThreadPool(threads);
    CountDownLatch latch = new CountDownLatch(threads);

    // when
    for (int i = 0; i < threads; i++) {
      executorService.submit(() -> {
        try {
          couponService.issueACoupon(coupon.getId(), user.getId());
        } catch (IllegalArgumentException e) {
          Assertions.assertThat(e.getMessage()).isEqualTo("같은 쿠폰은 하나만 발급 가능합니다.");
        } finally {
          latch.countDown();
        }
      });
    }
    latch.await();

    // then
    List<Coupon> result = couponRepository.getCouponsByUserId(user.getId());
    Assertions.assertThat(result.size()).isEqualTo(1);
  }


}
