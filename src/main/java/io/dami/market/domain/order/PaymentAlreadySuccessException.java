package io.dami.market.domain.order;

public class PaymentAlreadySuccessException extends RuntimeException {
    public PaymentAlreadySuccessException(String message) {
        super(message);
    }
}
