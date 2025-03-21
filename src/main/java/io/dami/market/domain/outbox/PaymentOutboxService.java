package io.dami.market.domain.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentOutboxService {

  private final PaymentOutboxRepository paymentOutboxRepository;


  @Transactional(readOnly = true)
  public PaymentOutbox getByPayload(String payload) {
    return paymentOutboxRepository.getByPayload(payload);
  }

}
