package io.dami.market.domain.product;

public class ProductIsOutOfStock extends RuntimeException {

  public ProductIsOutOfStock(String message) {
    super(message);
  }
}
