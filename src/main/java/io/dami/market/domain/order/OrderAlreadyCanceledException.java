package io.dami.market.domain.order;

public class OrderAlreadyCanceledException extends RuntimeException {

  public OrderAlreadyCanceledException(String message) {
    super(message);
  }
}
