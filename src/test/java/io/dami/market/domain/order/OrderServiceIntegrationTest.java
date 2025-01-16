package io.dami.market.domain.order;

import io.dami.market.domain.product.Product;
import io.dami.market.domain.product.ProductRepository;
import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserRepository;
import io.dami.market.utils.IntegrationServiceTest;
import io.dami.market.utils.fixture.ProductFixture;
import io.dami.market.utils.fixture.UserFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class OrderServiceIntegrationTest extends IntegrationServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected ProductRepository productRepository;

    @DisplayName("주문 시 결제 대기 상태로 주문 생성됨")
    @Test
    void 주문_결제_대기_성공() {
        // given
        User user = userRepository.save(UserFixture.user("박주닮"));
        int price = 5000;
        int quantity = 5;
        Product product = productRepository.save(ProductFixture.product("좋은데이", price));
        List<OrderCommand.OrderDetails> orderDetails = new ArrayList<>();
        orderDetails.add(new OrderCommand.OrderDetails(product.getId(), quantity));

        // when
        Order result = orderService.order(user.getId(), orderDetails);

        // then
        Assertions.assertThat(result.getTotalPrice().compareTo(BigDecimal.valueOf(price * quantity))).isEqualTo(0);
        Assertions.assertThat(result.getStatus()).isEqualTo(Order.OrderStatus.PENDING_PAYMENT);
    }

}
