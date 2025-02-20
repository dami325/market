package io.dami.market.domain.payment.event;

public record PayCompleteEvent(
    Long orderId,
    Long paymentId
) {

}
