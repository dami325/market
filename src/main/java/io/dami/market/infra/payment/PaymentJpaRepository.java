package io.dami.market.infra.payment;

import io.dami.market.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderIdAndPaymentStatus(Long orderId, Payment.PaymentStatue paymentStatus);

    @Query("""
    SELECT pay
    FROM Payment pay
    WHERE pay.orderId = :orderId
    AND pay.paymentStatus != 'FAIL'
    """)
    Optional<Payment> duplicateCheck(Long orderId);
}
