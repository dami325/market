package io.dami.market.domain.order;

public class PaymentAlreadyInProcessException extends RuntimeException {
    public PaymentAlreadyInProcessException(String message) {
        super(message);
    }
}
