package io.dami.market.domain.order;

import io.dami.market.domain.product.Product;
import io.dami.market.domain.product.ProductRepository;
import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserRepository;
import io.dami.market.utils.fixture.ProductFixture;
import io.dami.market.utils.fixture.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
    void 주문서_발행_성공() {
        // given
        Long userId = 5L;
        Long productAId = 15L;
        Long productBId = 16L;
        int productAPrice = 10000;
        int productBPrice = 20000;

        Product productA = ProductFixture.product(productAId, "productA", productAPrice);
        Product productB = ProductFixture.product(productBId, "productB", productBPrice);

        User user = UserFixture.user("박주닮");

        int orderQuantity = 3;
        List<OrderCommand.OrderDetails> orderDetails = List.of(
                new OrderCommand.OrderDetails(productAId, orderQuantity),
                new OrderCommand.OrderDetails(productBId, orderQuantity)
        );

        Order orderInit = Order.builder()
                .status(Order.OrderStatus.PENDING_PAYMENT)
                .user(user)
                .build();

        when(orderRepository.save(any(Order.class))).thenReturn(orderInit);
        when(userRepository.getUser(userId)).thenReturn(user);
        when(productRepository.getAllById(any())).thenReturn(List.of(productA, productB));

        // when
        Order order = orderService.order(userId, orderDetails);

        // then
        assertThat(order.getOrderDetails().size()).isEqualTo(2);
        // 결제 금액 확인
        assertThat(order.getTotalPrice()).isEqualTo(BigDecimal.valueOf((productAPrice * orderQuantity) + (productBPrice * orderQuantity)));
    }
}
