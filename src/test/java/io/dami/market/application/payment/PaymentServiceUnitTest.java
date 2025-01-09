package io.dami.market.application.payment;

import io.dami.market.domain.order.Order;
import io.dami.market.domain.order.OrderRepository;
import io.dami.market.domain.payment.Payment;
import io.dami.market.domain.payment.PaymentRepository;
import io.dami.market.domain.product.Product;
import io.dami.market.domain.product.ProductRepository;
import io.dami.market.domain.user.User;
import io.dami.market.utils.fixture.OrderFixture;
import io.dami.market.utils.fixture.ProductFixture;
import io.dami.market.utils.fixture.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

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
    private ProductRepository productRepository;
    @Mock
    private OrderRepository orderRepository;

    /**
     * 주문서 상태 재고부족처리
     * 결제 상태 실패 처리 검증
     */
    @Test
    void 결제_재고부족_실패() {
        // given
        int userPoint = 80000;
        int quantity = 10; // 주문 수량
        Long orderId = 10L;
        Product productA = ProductFixture.product("productA", 5000, 9); // 재고 9개로 세팅
        Order order = OrderFixture.order(UserFixture.user("박주닮", userPoint));
        order.addOrderDetail(productA, quantity); // 5000원짜리 상품 10개 주문

        when(paymentRepository.save(any(Payment.class))).thenReturn(new Payment(order));
        when(orderRepository.getOrderWithLock(orderId)).thenReturn(order);

        // when
        Payment payment = paymentService.pay(orderId);

        // then
        assertThat(payment.getPaymentStatus()).isEqualTo(Payment.PaymentStatue.FAIL);
        assertThat(order.getStatus()).isEqualTo(Order.OrderStatus.OUT_OF_STOCK);
    }

    @Test
    void 결제_포인트부족_실패() {
        // given
        int userPoint = 20000;
        int quantity = 10; // 주문 수량
        Long orderId = 10L;
        Product productA = ProductFixture.product("productA", 5000, 9); // 재고 9개로 세팅
        Order order = OrderFixture.order(UserFixture.user("박주닮", userPoint));
        order.addOrderDetail(productA, quantity); // 5000원짜리 상품 10개 주문

        when(paymentRepository.save(any(Payment.class))).thenReturn(new Payment(order));
        when(orderRepository.getOrderWithLock(orderId)).thenReturn(order);

        // when
        Payment payment = paymentService.pay(orderId);

        // then
        assertThat(payment.getPaymentStatus()).isEqualTo(Payment.PaymentStatue.FAIL);
        assertThat(order.getStatus()).isEqualTo(Order.OrderStatus.PENDING_PAYMENT);
    }

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
        Long orderId = 15L;
        User user = UserFixture.user("박주닮", userPoint);
        Order order = OrderFixture.order(user);
        Product productA = ProductFixture.product("productA", 5000);
        Integer beforeStockQuantity = productA.getStockQuantity();
        order.addOrderDetail(productA, quantity); // 5000원짜리 상품 10개 주문

        when(paymentRepository.save(any(Payment.class))).thenReturn(new Payment(order));
        when(orderRepository.getOrderWithLock(orderId)).thenReturn(order);

        // when
        Payment payment = paymentService.pay(orderId);

        // then
        assertThat(payment.getPaymentStatus()).isEqualTo(Payment.PaymentStatue.SUCCESS);
        assertThat(user.getUserPoint().getBalance()).isEqualTo(BigDecimal.valueOf(userPoint).subtract(order.getTotalPrice()));
        assertThat(beforeStockQuantity).isEqualTo(productA.getStockQuantity() + quantity);
        assertThat(order.getStatus()).isEqualTo(Order.OrderStatus.PAYMENT_SUCCESS);
    }
}
