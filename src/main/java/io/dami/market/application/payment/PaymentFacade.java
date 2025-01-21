package io.dami.market.application.payment;

import io.dami.market.domain.order.Order;
import io.dami.market.domain.order.OrderService;
import io.dami.market.domain.payment.Payment;
import io.dami.market.domain.payment.PaymentService;
import io.dami.market.domain.point.PointService;
import io.dami.market.domain.product.ProductService;
import io.dami.market.infra.dataplatform.DataPlatform;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

    private final PaymentService paymentService;
    private final ProductService productService;
    private final PointService pointService;
    private final OrderService orderService;
    private final DataPlatform dataPlatform;

    @Transactional
    public void processOrderPayment(Long userId, Long orderId) {
        Order order = orderService.getCompleteOrder(orderId);
        productService.quantitySubtract(order.getProductQuantityMap());
        Payment payment = paymentService.pay(orderId, order.getTotalAmount());
        pointService.usePoints(userId, payment.getTotalAmount());
        dataPlatform.publish(orderId, payment.getId());
    }
}
