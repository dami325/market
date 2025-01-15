package io.dami.market.application.payment;

import io.dami.market.domain.coupon.Coupon;
import io.dami.market.domain.order.Order;
import io.dami.market.domain.order.OrderRepository;
import io.dami.market.domain.payment.Payment;
import io.dami.market.domain.payment.PaymentRepository;
import io.dami.market.domain.product.Product;
import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserCoupon;
import io.dami.market.utils.fixture.CouponFixture;
import io.dami.market.utils.fixture.OrderFixture;
import io.dami.market.utils.fixture.ProductFixture;
import io.dami.market.utils.fixture.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceUnitTest {

    @InjectMocks
    private PaymentService paymentService;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OrderRepository orderRepository;

    /**
     * 재고 정상 차감
     * 포인트 정상 차감
     * 주문서, 결제 성공 상태 검증
     */
    @Test
    void 주문서로_결제_진행_성공() {
        // given
        int userPoint = 80000;
        int quantity = 10;
        User user = UserFixture.user("박주닮", userPoint);
        Order order = OrderFixture.order(user);
        Product productA = ProductFixture.product("productA", 5000);
        order.addOrderDetail(productA, quantity); // 5000원짜리 상품 10개 주문
        Coupon coupon = CouponFixture.coupon("새해맞이쿠폰");
        coupon.issue(user);
        UserCoupon userCoupon = user.getUserCoupons().iterator().next();

        when(paymentRepository.save(any(Payment.class))).thenReturn(new Payment(order, userCoupon));

        // when
        Payment payment = paymentService.pay(order, userCoupon);

        // then
        assertThat(payment.getPaymentStatus()).isEqualTo(Payment.PaymentStatue.SUCCESS);
        assertThat(order.getStatus()).isEqualTo(Order.OrderStatus.PAYMENT_SUCCESS);
    }
}
