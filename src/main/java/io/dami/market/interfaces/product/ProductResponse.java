package io.dami.market.interfaces.product;

import io.dami.market.domain.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record ProductResponse(

) {
  public record ProductDetails(
      @Schema(example = "5")
      Long productId,
      @Schema(example = "진로")
      String name,
      @Schema(example = "5300")
      BigDecimal price,
      @Schema(example = "3")
      Integer stockQuantity
  ) {

    public ProductDetails(Product product) {
      this(product.getId(), product.getName(), product.getPrice(), product.getStockQuantity());
    }
  }
}
