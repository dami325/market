package io.dami.market.domain.point;

import io.dami.market.domain.payment.PointNotEnoughException;
import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserRepository;
import io.dami.market.utils.IntegrationServiceTest;
import io.dami.market.utils.fixture.PointFixture;
import io.dami.market.utils.fixture.UserFixture;
import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PointServiceIntegrationTest extends IntegrationServiceTest {

  @Autowired
  private PointService pointService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PointRepository pointRepository;

  @Test
  void 포인트_조회_성공() {
    // given
    User user = userRepository.save(UserFixture.user("박주닮"));
    Point point = pointRepository.save(PointFixture.point(user.getId(), 5000));

    // when
    BigDecimal balance = pointService.getBalance(user.getId());

    // then
    Assertions.assertThat(balance.compareTo(point.getTotalPoint())).isEqualTo(0);
  }

  @Test
  void 포인트_충전_성공() {
    // given
    User user = userRepository.save(UserFixture.user("박주닮"));
    Point point = pointRepository.save(PointFixture.point(user.getId(), 5000));

    // when
    pointService.chargePoint(user.getId(), BigDecimal.valueOf(12000));

    // then
    Point resultPoint = pointRepository.getPointByUserId(user.getId());
    Assertions.assertThat(resultPoint.getTotalPoint().compareTo(BigDecimal.valueOf(17000)))
        .isEqualTo(0);
  }

  @Test
  void 포인트_부족_사용_실패() {
    // given
    User user = userRepository.save(UserFixture.user("박주닮"));
    Point point = pointRepository.save(PointFixture.point(user.getId(), 5000));

    // when && then
    Assertions.assertThatThrownBy(
            () -> pointService.usePoints(user.getId(), BigDecimal.valueOf(6000)), "포인트 부족 사용 실패")
        .isInstanceOf(PointNotEnoughException.class)
        .hasMessageContaining("포인트 부족 사용 실패");
  }

  @DisplayName("포인트 차감 동시 요청 시 순차적 감소 락 검증")
  @Test
  void 포인트_차감_동시_요청_시_순차적_감소_락검증_성공() throws InterruptedException {
    // given
    int point = 9000;
    User user = userRepository.save(UserFixture.user("박주닮"));
    pointRepository.save(PointFixture.point(user.getId(), point));
    int usePoint = 3000;
    int threads = 3;

    ExecutorService executorService = Executors.newFixedThreadPool(threads);
    CountDownLatch latch = new CountDownLatch(threads);

    // when
    for (int i = 0; i < threads; i++) {
      executorService.submit(() -> {
        try {
          pointService.usePoints(user.getId(), BigDecimal.valueOf(usePoint));
        } finally {
          latch.countDown();
        }
      });
    }
    latch.await();
    executorService.shutdown();

    // then
    Point resultPoint = pointRepository.getPointByUserId(user.getId());
    Assertions.assertThat(resultPoint.getTotalPoint().compareTo(BigDecimal.ZERO)).isEqualTo(0);

  }

  @DisplayName("특정 유저의 포인트 충전 동시에 10번 충전 정합성 검증")
  @Test
  void 특정_유저의_포인트_충전_동시에_10번() throws InterruptedException {
    // given
    int initPoint = 1500;
    int chargePoint = 8000;
    User user = userRepository.save(UserFixture.user("박주닮"));
    pointRepository.save(PointFixture.point(user.getId(), initPoint));

    int threads = 10;
    ExecutorService executorService = Executors.newFixedThreadPool(threads);
    CountDownLatch latch = new CountDownLatch(threads);

    // when
    for (int i = 0; i < threads; i++) {
      executorService.submit(() -> {
        try {
          pointService.chargePoint(user.getId(), BigDecimal.valueOf(chargePoint));
        } finally {
          latch.countDown();
        }
      });
    }
    latch.await();
    executorService.shutdown();

    // then
    Point resultPoint = pointRepository.getPointByUserId(user.getId());
    Assertions.assertThat(
            resultPoint.getTotalPoint()
                .compareTo(BigDecimal.valueOf(chargePoint * threads + initPoint)))
        .isEqualTo(0);

  }

}
