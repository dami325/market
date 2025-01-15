package io.dami.market.domain.payment;

public class PointNotEnoughException extends RuntimeException {
    public PointNotEnoughException(String message) {
        super(message);
    }
}
