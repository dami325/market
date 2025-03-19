package io.dami.market.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;

import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserRepository;
import io.dami.market.utils.IntegrationServiceTest;
import io.dami.market.utils.fixture.CouponFixture;
import io.dami.market.utils.fixture.UserFixture;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.transaction.annotation.Transactional;

class CouponServiceIntegrationServiceTest extends IntegrationServiceTest {

  @Autowired
  private CouponService couponService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CouponRepository couponRepository;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  private static final String COUPON_REQUEST_KEY = "coupon-%d-requests";
  private static final String COUPON_ISSUED_KEY = "coupon-%d-issued";

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
    couponService.issueACouponV1(coupon.getId(), user.getId());

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
          couponService.issueACouponV1(coupon.getId(), user.getId());
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

  @DisplayName("선착순 쿠폰 발급 100명이 동시에 요청")
  @Test
  void 선착순_쿠폰_발급_100명() throws InterruptedException {
    // given
    int userCount = 100;
    int totalQuantity = 100;
    int issuedQuantity = 0;

    List<User> users = new ArrayList<>();
    for (int i = 1; i <= userCount; i++) {
      users.add(userRepository.save(UserFixture.user("USER" + i)));
    }

    Coupon coupon = couponRepository.save(CouponFixture.coupon("새해쿠폰", totalQuantity,
        issuedQuantity));

    ExecutorService executorService = Executors.newFixedThreadPool(userCount);
    CountDownLatch latch = new CountDownLatch(userCount);

    // when
    for (User user : users) {
      executorService.submit(() -> {
        try {
          couponService.issueACouponV1(coupon.getId(), user.getId());
        } catch (IllegalArgumentException e) {
          Assertions.assertThat(e.getMessage()).isEqualTo("같은 쿠폰은 하나만 발급 가능합니다.");
        } finally {
          latch.countDown();
        }
      });
    }
    latch.await();

    // then
    Coupon result = couponRepository.getCoupon(coupon.getId());
    Assertions.assertThat(result.getIssuedQuantity()).isEqualTo(userCount);
  }


  @Test
  void 선착순_쿠폰_발급요청_레디스저장_테스트() {
    // given
    User user = userRepository.save(UserFixture.user("박주닮"));
    Coupon coupon = couponRepository.save(CouponFixture.coupon("새해쿠폰"));

    // when
    couponService.issueACouponV2(coupon.getId(), user.getId());

    // then
    String requestKey = String.format(COUPON_REQUEST_KEY, coupon.getId());
    Set<String> result = redisTemplate.opsForZSet()
        .popMin(requestKey, coupon.getTotalQuantity() - coupon.getIssuedQuantity())
        .stream().map(TypedTuple::getValue)
        .collect(Collectors.toSet());
    Assertions.assertThat(result.size()).isEqualTo(1);
  }

  @Test
  void 선착순_쿠폰_발급_레디스_테스트() {
    // given
    User user = userRepository.save(UserFixture.user("박주닮"));
    Coupon coupon = couponRepository.save(CouponFixture.coupon("새해쿠폰"));
    couponService.issueACouponV2(coupon.getId(), user.getId());
    int beforeIssuedQuantity = coupon.getIssuedQuantity();

    // when
    couponService.processCouponIssuance(coupon.getId(),
        coupon.getTotalQuantity() - beforeIssuedQuantity);

    // then
    int issuedQuantity = couponRepository.getCoupon(coupon.getId())
        .getIssuedQuantity();
    Assertions.assertThat(issuedQuantity).isEqualTo(beforeIssuedQuantity + 1);

  }
}
