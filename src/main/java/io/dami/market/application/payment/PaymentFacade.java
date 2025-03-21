package io.dami.market.application.payment;

import io.dami.market.domain.order.Order;
import io.dami.market.domain.order.OrderService;
import io.dami.market.domain.payment.Payment;
import io.dami.market.domain.payment.PaymentService;
import io.dami.market.application.event.PayCompleteEvent;
import io.dami.market.domain.point.PointService;
import io.dami.market.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

  private final PaymentService paymentService;
  private final ProductService productService;
  private final PointService pointService;
  private final OrderService orderService;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public void processOrderPayment(Long userId, Long orderId) {
    Order order = orderService.getCompleteOrder(orderId);
    productService.quantitySubtract(order.getProductQuantityMap());
    Payment payment = paymentService.pay(orderId, order.getTotalAmount());
    pointService.usePoints(userId, payment.getTotalAmount());
    eventPublisher.publishEvent(new PayCompleteEvent(orderId, payment.getId()));
  }
}
