package io.dami.market.infrastructure.outbox;

import io.dami.market.domain.outbox.PaymentOutbox;
import io.dami.market.domain.outbox.PaymentOutboxRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentOutboxRepositoryImpl implements PaymentOutboxRepository {

  private final PaymentOutboxJpaRepository paymentOutboxJpaRepository;

  @Override
  public PaymentOutbox savePaymentOutbox(PaymentOutbox paymentOutbox) {
    return paymentOutboxJpaRepository.save(paymentOutbox);
  }

  @Override
  public PaymentOutbox getByPayload(String payload) {
    return paymentOutboxJpaRepository.findByPayload(payload)
        .orElseThrow(EntityNotFoundException::new);
  }

  @Override
  public List<PaymentOutbox> getReissueOutboxes() {
    return paymentOutboxJpaRepository.getReissueOutboxes();
  }

}
