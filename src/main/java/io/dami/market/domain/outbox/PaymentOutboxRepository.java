package io.dami.market.domain.outbox;


import java.util.List;

public interface PaymentOutboxRepository {

  PaymentOutbox savePaymentOutbox(PaymentOutbox paymentOutbox);

  PaymentOutbox getByPayload(String message);

  List<PaymentOutbox> getReissueOutboxes();
}
