package io.dami.market.application.payment;

import io.dami.market.domain.order.Order;
import io.dami.market.domain.order.OrderRepository;
import io.dami.market.domain.payment.Payment;
import io.dami.market.domain.payment.PaymentRepository;
import io.dami.market.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Payment pay(Long orderId) {
        Order order = orderRepository.getOrderWithLock(orderId); // 주문 배타락 상태 변경 방지 및 중복 결제 방지
        productRepository.findAllByIdWithLock(order.getOrderProductIds()); // 상품 배타락

        Payment payment = paymentRepository.save(new Payment(order));
        payment.pay();
        return payment;
    }
}
