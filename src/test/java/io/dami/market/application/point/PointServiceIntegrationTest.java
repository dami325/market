package io.dami.market.application.point;

import io.dami.market.domain.payment.PointNotEnoughException;
import io.dami.market.domain.point.PointService;
import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserRepository;
import io.dami.market.utils.IntegrationServiceTest;
import io.dami.market.utils.fixture.UserFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

class PointServiceIntegrationTest extends IntegrationServiceTest {

    @Autowired
    private PointService pointService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 포인트_조회_성공() {
        // given
        User user = userRepository.save(UserFixture.user("박주닮", 5000));

        // when
        BigDecimal balance = pointService.getBalance(user.getId());

        // then
        Assertions.assertThat(balance.compareTo(userRepository.getUser(user.getId()).getUserPoint().getBalance())).isEqualTo(0);
    }

    @Test
    void 포인트_충전_성공() {
        // given
        User user = userRepository.save(UserFixture.user("박주닮", 5000));

        // when
        pointService.chargePoint(user.getId(), BigDecimal.valueOf(12000));

        // then
        User result = userRepository.getUser(user.getId());
        Assertions.assertThat(result.getUserPoint().getBalance().compareTo(BigDecimal.valueOf(17000))).isEqualTo(0);
    }

    @Test
    void 포인트_부족_사용_실패() {
        // given
        User user = userRepository.save(UserFixture.user("박주닮", 5000));

        // when && then
        Assertions.assertThatThrownBy(() -> pointService.usePoints(user.getId(), BigDecimal.valueOf(6000)), "포인트 부족 사용 실패")
                .isInstanceOf(PointNotEnoughException.class)
                .hasMessageContaining("포인트 부족 사용 실패");
    }

}
