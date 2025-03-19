package io.dami.market.interfaces.product;

import com.querydsl.core.annotations.QueryProjection;
import io.dami.market.domain.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record ProductResponse(

) {

  public record Top5ProductDetails(
      @Schema(example = "10")
      Long productId,
      @Schema(example = "좋은데이")
      String name,
      @Schema(example = "5500")
      BigDecimal price,
      @Schema(example = "15")
      Integer stockQuantity,
      @Schema(example = "500")
      int total_quantity_sold
  ) {

    @QueryProjection
    public Top5ProductDetails {
    }
  }

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
