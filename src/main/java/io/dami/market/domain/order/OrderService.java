package io.dami.market.domain.order;

import io.dami.market.domain.product.Product;
import io.dami.market.domain.product.ProductRepository;
import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public Order order(Long userId, List<OrderCommand.OrderDetails> orderDetails) {
        User user = userRepository.getUser(userId);

        Map<Long, Integer> productOrderMap = orderDetails.stream()
                .collect(Collectors.toMap(OrderCommand.OrderDetails::productId, OrderCommand.OrderDetails::quantity));

        List<Product> products = productRepository.getAllById(productOrderMap.keySet());

        Order order = orderRepository.save(Order.builder()
                .status(Order.OrderStatus.PENDING_PAYMENT)
                .user(user)
                .build());

        products.forEach(product -> {
            int quantity = productOrderMap.get(product.getId());
            order.addOrderDetail(product, quantity);
        });

        return order;
    }

    @Transactional
    public Order getOrderWithLock(Long orderId) {
        return orderRepository.getOrderWithLock(orderId);
    }
}
