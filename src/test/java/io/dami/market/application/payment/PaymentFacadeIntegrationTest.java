package io.dami.market.application.payment;

import io.dami.market.domain.coupon.Coupon;
import io.dami.market.domain.coupon.CouponRepository;
import io.dami.market.domain.order.Order;
import io.dami.market.domain.order.OrderCommand;
import io.dami.market.domain.order.OrderService;
import io.dami.market.domain.payment.Payment;
import io.dami.market.domain.product.Product;
import io.dami.market.domain.product.ProductIsOutOfStock;
import io.dami.market.domain.product.ProductRepository;
import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserCoupon;
import io.dami.market.domain.user.UserRepository;
import io.dami.market.infra.coupon.UserCouponJpaRepository;
import io.dami.market.utils.IntegrationServiceTest;
import io.dami.market.utils.fixture.CouponFixture;
import io.dami.market.utils.fixture.ProductFixture;
import io.dami.market.utils.fixture.UserCouponFixture;
import io.dami.market.utils.fixture.UserFixture;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

public class PaymentFacadeIntegrationTest extends IntegrationServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponJpaRepository userCouponJpaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private PaymentFacade paymentFacade;

    @Test
    void 결제_주문상태_할인적용_결제금액_결제상태_검증_성공() {
        // given
        Coupon coupon = couponRepository.save(CouponFixture.coupon("새해쿠폰", new BigDecimal("10000")));
        Product productA = productRepository.save(ProductFixture.product("productA", 10000));
        Product productB = productRepository.save(ProductFixture.product("productB", 20000));
        User user = userRepository.save(UserFixture.user("박주닮", 90000));
        UserCoupon userCoupon = userCouponJpaRepository.save(UserCouponFixture.userCoupon(coupon, user));
        int orderQuantity = 3;

        List<OrderCommand.OrderDetails> orderDetails = List.of(
                new OrderCommand.OrderDetails(productA.getId(), orderQuantity), // 3만원
                new OrderCommand.OrderDetails(productB.getId(), orderQuantity) // 6만원
        );
        Order order = orderService.order(user.getId(), orderDetails);

        // when
        paymentFacade.pay(user.getId(), order.getId(), userCoupon.getId());


        // then 테스트 에서만 쓰는 쿼리 만들기 싫어서 EntityManager 로 구현
        Order resultOrder = (Order) em.createQuery("select orders from Order orders where orders.user.id = :userid")
                .setParameter("userid", user.getId())
                .getSingleResult();

        Payment resultPayment = (Payment) em.createQuery("select pay from Payment pay where pay.order.id = :orderId")
                .setParameter("orderId", resultOrder.getId())
                .getSingleResult();

        // 외부 데이터 플랫폼 전송은 Publishing to Mock Data Platform. Order ID: 1, Payment ID: 1 로그로 확인
        Assertions.assertThat(resultOrder.getStatus()).isEqualTo(Order.OrderStatus.PAYMENT_SUCCESS); // 주문 상태 검증
        Assertions.assertThat(resultPayment.getDiscountAmount().compareTo(coupon.getDiscountAmount())).isEqualTo(0); // 할인적용 검증
        Assertions.assertThat(resultPayment.getPaymentStatus()).isEqualTo(Payment.PaymentStatue.SUCCESS); // 결제 상태 검증
        Assertions.assertThat(resultPayment.getAmount()).isEqualTo(resultOrder.getTotalPrice().subtract(coupon.getDiscountAmount())); // 결제금액 검증
    }

    @Test
    void 주문_결제_재고없음_실패() {
        // given
        Coupon coupon = couponRepository.save(CouponFixture.coupon("새해쿠폰", new BigDecimal("10000")));
        Product productA = productRepository.save(ProductFixture.product("productA", 10000, 1));
        Product productB = productRepository.save(ProductFixture.product("productB", 20000, 2));
        User user = userRepository.save(UserFixture.user("박주닮", 90000));
        UserCoupon userCoupon = userCouponJpaRepository.save(UserCouponFixture.userCoupon(coupon, user));
        int orderQuantity = 3;

        List<OrderCommand.OrderDetails> orderDetails = List.of(
                new OrderCommand.OrderDetails(productA.getId(), orderQuantity), // 3만원
                new OrderCommand.OrderDetails(productB.getId(), orderQuantity) // 6만원
        );
        Order order = orderService.order(user.getId(), orderDetails);

        // when & then
        Assertions.assertThatThrownBy(() -> paymentFacade.pay(user.getId(), order.getId(), userCoupon.getId()), "결제 재고 없어서 실패")
                .isInstanceOf(ProductIsOutOfStock.class)
                .hasMessageContaining("재고가 부족합니다");

    }

    @Test
    void 주문_결제_포인트_없음_실패() {
        // given
        Coupon coupon = couponRepository.save(CouponFixture.coupon("새해쿠폰", new BigDecimal("10000")));
        Product productA = productRepository.save(ProductFixture.product("productA", 10000));
        Product productB = productRepository.save(ProductFixture.product("productB", 20000));
        User user = userRepository.save(UserFixture.user("박주닮", 10000));
        UserCoupon userCoupon = userCouponJpaRepository.save(UserCouponFixture.userCoupon(coupon, user));
        int orderQuantity = 3;

        List<OrderCommand.OrderDetails> orderDetails = List.of(
                new OrderCommand.OrderDetails(productA.getId(), orderQuantity), // 3만원
                new OrderCommand.OrderDetails(productB.getId(), orderQuantity) // 6만원
        );
        Order order = orderService.order(user.getId(), orderDetails);

        // when & then
        Assertions.assertThatThrownBy(() -> paymentFacade.pay(user.getId(), order.getId(), userCoupon.getId()), "포인트 부족해서 실패")
                .hasMessageContaining("포인트 부족 사용 실패");

    }
}
