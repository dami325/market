package io.dami.market.interfaces.order;

import io.dami.market.domain.order.OrderCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record OrderRequest() {

  public record CreateOrder(
      @Schema(example = "5")
      @NotNull
      Long userId,
      Long issuedCouponId,
      @NotEmpty
      List<OrderDetails> products
  ) {

    public record OrderDetails(
        @Schema(example = "5")
        Long productId,
        @Schema(example = "3")
        int quantity
    ) {

    }

    public OrderCommand.CreateOrder toCommand() {
      return new OrderCommand.CreateOrder(
          userId,
          issuedCouponId,
          products.stream()
              .map(
                  product -> new OrderCommand.ProductStock(product.productId(), product.quantity()))
              .toList());
    }
  }
}
