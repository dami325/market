package io.dami.market.domain.point;

import io.dami.market.domain.point.PointService;
import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserRepository;
import io.dami.market.utils.fixture.UserFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointServiceUnitTest {

    @InjectMocks
    private PointService pointService;

    @Mock
    private UserRepository userRepository;

    @Test
    void 포인트_충전_0이하는_실패() {
        // given
        Long userId = 5L;
        BigDecimal amount = BigDecimal.ZERO;
        User user = UserFixture.user("박주닮");

        when(userRepository.getUser(userId)).thenReturn(user);

        // when & then
        Assertions.assertThatThrownBy(() -> pointService.chargePoint(userId,amount))
                .hasMessageContaining("0 이하는 충전불가");
    }

    @Test
    void 포인트_충전_성공() {
        // given
        Long userId = 5L;
        BigDecimal amount = BigDecimal.valueOf(1000);
        User user = UserFixture.user("박주닮");
        BigDecimal before = user.getUserPoint().getBalance();

        when(userRepository.getUser(userId)).thenReturn(user);

        // when & then
        pointService.chargePoint(userId,amount);

        Assertions.assertThat(before.add(amount)).isEqualTo(user.getUserPoint().getBalance());
    }
}
