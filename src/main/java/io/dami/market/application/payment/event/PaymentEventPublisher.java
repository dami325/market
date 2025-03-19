package io.dami.market.application.payment.event;

public interface PaymentEventPublisher {

  void sendMessage(PayCompleteEvent event);
}
