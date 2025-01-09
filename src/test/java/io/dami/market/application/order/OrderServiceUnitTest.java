package io.dami.market.application.order;

import io.dami.market.domain.coupon.Coupon;
import io.dami.market.domain.order.Order;
import io.dami.market.domain.order.OrderCommand;
import io.dami.market.domain.order.OrderRepository;
import io.dami.market.domain.product.Product;
import io.dami.market.domain.product.ProductRepository;
import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserRepository;
import io.dami.market.utils.fixture.CouponFixture;
import io.dami.market.utils.fixture.ProductFixture;
import io.dami.market.utils.fixture.UserFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceUnitTest {

    @InjectMocks
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;

    @Test
    void 주문서_쿠폰_적용_발행_성공() {
        // given
        Long userId = 5L;
        Long couponId = 5L;
        Long productAId = 15L;
        Long productBId = 16L;
        int productAPrice = 10000;
        int productBPrice = 20000;
        BigDecimal discountAmount = BigDecimal.valueOf(10000);

        Coupon coupon = CouponFixture.coupon(couponId,"새해쿠폰", discountAmount);
        Product productA = ProductFixture.product("productA", productAPrice);
        Product productB = ProductFixture.product("productB", productBPrice);

        User user = UserFixture.user("박주닮");

        user.addCoupon(coupon);// 주문 시 사용할 쿠폰 세팅

        int orderQuantity = 3;
        List<OrderCommand.Product> products = List.of(
                new OrderCommand.Product(productAId, orderQuantity),
                new OrderCommand.Product(productBId, orderQuantity)
        );
        OrderCommand.DoOrder command = new OrderCommand.DoOrder(userId, couponId, products);

        Order orderInit = Order.builder()
                .status(Order.OrderStatus.PENDING_PAYMENT)
                .user(user)
                .build();

        when(orderRepository.save(any(Order.class))).thenReturn(orderInit);
        when(userRepository.getUser(userId)).thenReturn(user);
        when(productRepository.getProduct(productAId)).thenReturn(productA);
        when(productRepository.getProduct(productBId)).thenReturn(productB);

        // when
        Order order = orderService.order(command);

        // then
        assertThat(order.getOrderDetails().size()).isEqualTo(2);
        // 결제 & 할인 금액 확인
        assertThat(order.getTotalPrice()).isEqualTo(BigDecimal.valueOf((productAPrice * orderQuantity) + (productBPrice * orderQuantity) - discountAmount.intValue()));
        assertThat(order.getDiscountAmount()).isEqualTo(discountAmount);
    }
}
