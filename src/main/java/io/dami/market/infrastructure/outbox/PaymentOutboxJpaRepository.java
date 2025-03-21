package io.dami.market.infrastructure.outbox;

import io.dami.market.domain.outbox.PaymentOutbox;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentOutboxJpaRepository extends JpaRepository<PaymentOutbox, Long> {

  @Query("""
      select pob
      from PaymentOutbox pob
      where pob.payload = :payload
      """)
  Optional<PaymentOutbox> findByPayload(String payload);

  @Query("""
      select pob
      from PaymentOutbox pob
      where pob.status != 'SUCCESS'
      """)
  List<PaymentOutbox> getReissueOutboxes();
}
