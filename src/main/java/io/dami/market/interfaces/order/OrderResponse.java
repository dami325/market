package io.dami.market.interfaces.order;

import io.dami.market.domain.order.Order;

public record OrderResponse() {

  public record OrderInfo(
      Long orderId
  ) {

    public OrderInfo(Order order) {
      this(order.getId());
    }
  }
}
