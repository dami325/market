package io.dami.market.application.order;

import io.dami.market.domain.order.Order;
import io.dami.market.domain.order.OrderCommand;
import io.dami.market.domain.order.OrderRepository;
import io.dami.market.domain.product.Product;
import io.dami.market.domain.product.ProductRepository;
import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserCoupon;
import io.dami.market.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order order(OrderCommand.DoOrder command) {

        User user = userRepository.getUser(command.userId());
        Optional<UserCoupon> userCoupon = user.findUserCoupon(command.couponId()); // 쿠폰 필수 아님

        Order order = orderRepository.save(Order.builder()
                .status(Order.OrderStatus.PENDING_PAYMENT)
                .user(user)
                .coupon(userCoupon.map(UserCoupon::getCoupon).orElse(null))
                .build());

        for (OrderCommand.Product orderProduct : command.products()) {
            Product product = productRepository.getProduct(orderProduct.productId());
            order.addOrderDetail(product, orderProduct.quantity());
        }
        // 할인 적용
        order.disCount(userCoupon.map(UserCoupon::useCoupon).orElse(BigDecimal.ZERO));
        return order;
    }
}
