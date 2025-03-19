package io.dami.market.domain.order;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record OrderCommand() {

  public record CreateOrder(
      Long userId,
      Long issuedCouponId,
      List<ProductStock> productStocks
  ) {

    public Set<Long> productIds() {
      return this.productStocks.stream()
          .map(ProductStock::productId)
          .collect(Collectors.toSet());
    }

  }

  public record ProductStock(
      Long productId,
      int quantity
  ) {

  }
}
