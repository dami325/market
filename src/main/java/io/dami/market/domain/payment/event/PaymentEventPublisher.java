package io.dami.market.domain.payment.event;

public interface PaymentEventPublisher {

  void sendMessage(PayCompleteEvent event);
}
