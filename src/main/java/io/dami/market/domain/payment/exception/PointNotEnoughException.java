package io.dami.market.domain.payment.exception;

public class PointNotEnoughException extends RuntimeException {
    public PointNotEnoughException(String message) {
        super(message);
    }
}
