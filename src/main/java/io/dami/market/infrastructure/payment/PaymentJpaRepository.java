package io.dami.market.infrastructure.payment;

import io.dami.market.domain.payment.Payment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

  @Query("""
      SELECT pay
      FROM Payment pay
      WHERE pay.orderId = :orderId
      AND pay.paymentStatus != 'FAIL'
      """)
  Optional<Payment> duplicateCheck(Long orderId);
}
