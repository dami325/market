package io.dami.market.application.coupon;

import io.dami.market.domain.coupon.Coupon;
import io.dami.market.domain.coupon.CouponRepository;
import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserRepository;
import io.dami.market.utils.fixture.CouponFixture;
import io.dami.market.utils.fixture.UserFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceUnitTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void 선착순_쿠폰_조회_성공() {
        //given
        Long userId = 5L;
        List<Coupon> coupons = List.of(CouponFixture.coupon("새해맞이쿠폰"));
        when(couponRepository.getFirstServedCoupons(userId)).thenReturn(coupons);

        //when
        List<Coupon> result = couponService.getFirstServedCoupons(userId);

        //then
        verify(couponRepository, times(1)).getFirstServedCoupons(userId);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }


    @Test
    void 쿠폰_발급_수량_소진_실패() {
        // given
        Long couponId = 10L;
        Long userId = 5L;
        int totalQuantity = 10; // 총 수량
        int issuedQuantity = 10; // 발행된 수량
        Coupon coupon = CouponFixture.coupon("새해맞이쿠폰", totalQuantity, issuedQuantity);
        User user = UserFixture.user("박주닮");

        when(couponRepository.getCouponWithLock(couponId)).thenReturn(coupon);
        when(userRepository.getUser(userId)).thenReturn(user);

        // when & then
        Assertions.assertThatThrownBy(() -> couponService.issueACoupon(couponId,userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("발급 수량 소진");
    }

    @Test
    void 쿠폰_발급_만료일이_지나_실패() {
        // given
        Long couponId = 10L;
        Long userId = 5L;
        LocalDateTime endDate = LocalDateTime.now().minusDays(1L); // 오늘 기준 하루 지남
        Coupon coupon = CouponFixture.coupon("새해맞이쿠폰", endDate);
        User user = UserFixture.user("박주닮");

        when(couponRepository.getCouponWithLock(couponId)).thenReturn(coupon);
        when(userRepository.getUser(userId)).thenReturn(user);

        // when & then
        Assertions.assertThatThrownBy(() -> couponService.issueACoupon(couponId,userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("만료된 쿠폰");
    }

    @Test
    void 쿠폰_발급_같은쿠폰은_하나만_발급가능합니다_실패() {
        // given
        Long couponId = 10L;
        Long userId = 5L;
        Coupon coupon = CouponFixture.coupon("새해맞이쿠폰");
        User user = UserFixture.user("박주닮");

        when(couponRepository.getCouponWithLock(couponId)).thenReturn(coupon);
        when(userRepository.getUser(userId)).thenReturn(user);

        // when & then
        couponService.issueACoupon(couponId,userId); // 한번 발급

        // 중복발급
        Assertions.assertThatThrownBy(() -> couponService.issueACoupon(couponId,userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("같은 쿠폰은 하나만 발급 가능합니다.");
    }

    @Test
    void 쿠폰_발급_성공() {
        // given
        Long couponId = 10L;
        Long userId = 5L;
        Coupon coupon = CouponFixture.coupon("새해맞이쿠폰");
        User user = UserFixture.user("박주닮");
        int issuedQuantity = coupon.getIssuedQuantity(); // 발급 전 수량

        when(couponRepository.getCouponWithLock(couponId)).thenReturn(coupon);
        when(userRepository.getUser(userId)).thenReturn(user);

        // when
        couponService.issueACoupon(couponId,userId); // 한번 발급

        // then
        Assertions.assertThat(coupon.getIssuedQuantity()).isEqualTo(issuedQuantity + 1); // 발급 후 수량
        Assertions.assertThat(user.getUserCoupons().size()).isEqualTo(1);
    }

}
