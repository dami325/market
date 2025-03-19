package io.dami.market.domain.order;

import io.dami.market.domain.product.Product;
import java.math.BigDecimal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;

  @Transactional
  public Order getCompleteOrder(
      Long orderId
  ) {
    return orderRepository.getCompleteOrder(orderId);
  }

  @Transactional
  public Order createOrder(
      OrderCommand.CreateOrder commend,
      BigDecimal discountAmount,
      Map<Long, Product> productMap
  ) {
    Order order = orderRepository.save(
        Order.createOrderForm(commend.userId(), commend.issuedCouponId()));

    commend.productStocks().forEach(productStock -> {
      Product product = productMap.get(productStock.productId());
      OrderDetail orderDetail = OrderDetail.createOrderDetail(order, product.getId(),
          productStock.quantity(), product.getTotalPrice(productStock.quantity()));
      order.addOrderDetail(orderDetail);
    });

    order.discountApply(discountAmount);
    return order;
  }
}
