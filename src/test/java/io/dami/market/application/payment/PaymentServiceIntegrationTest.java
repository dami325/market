package io.dami.market.application.payment;

import io.dami.market.domain.coupon.Coupon;
import io.dami.market.domain.coupon.CouponRepository;
import io.dami.market.domain.order.Order;
import io.dami.market.domain.order.OrderRepository;
import io.dami.market.domain.payment.Payment;
import io.dami.market.domain.product.Product;
import io.dami.market.domain.product.ProductRepository;
import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserCoupon;
import io.dami.market.domain.user.UserRepository;
import io.dami.market.infra.coupon.UserCouponJpaRepository;
import io.dami.market.utils.IntegrationServiceTest;
import io.dami.market.utils.fixture.*;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

class PaymentServiceIntegrationTest extends IntegrationServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponJpaRepository userCouponJpaRepository;

    @Autowired
    private EntityManager em;

    @DisplayName("""
            총 주문 금액보다 할인 금액이 큰 경우
            할인 금액을 주문 총액으로 조정한다.
            ex) 
            총 금액 36,000원
            쿠폰 할인금액 125,000원
            
            payment
            할인된 금액 36,000원
            총 금액 0원
            """)
    @Test
    void pay() {
        // given
        BigDecimal discountAmount = new BigDecimal("125000");
        int quantity = 3;
        int priceA = 5000;
        int priceB = 4000;
        int priceC = 3000;
        BigDecimal totalAmount = BigDecimal.valueOf((priceA + priceB + priceC) * quantity);
        List<Product> products = productRepository.saveAll(
                ProductFixture.products(
                        Map.of(
                                "좋은데이", priceA,
                                "참이슬", priceB,
                                "테라", priceC
                        )
                )
        );
        User user = userRepository.save(UserFixture.user("박주닮"));
        Coupon coupon = couponRepository.save(CouponFixture.coupon("새해맞이 쿠폰", discountAmount));
        UserCoupon userCoupon = userCouponJpaRepository.save(UserCouponFixture.userCoupon(coupon, user));
        Order order = orderRepository.save(OrderFixture.order(user, quantity, products.toArray(Product[]::new)));

        // when
        Payment result = paymentService.pay(order, userCoupon);

        // then
        Assertions.assertThat(result.getDiscountAmount().compareTo(totalAmount)).isEqualTo(0);
        Assertions.assertThat(result.getPaymentStatus()).isEqualTo(Payment.PaymentStatue.SUCCESS);
        Assertions.assertThat(result.getAmount()).isEqualTo(BigDecimal.ZERO);
    }
}
