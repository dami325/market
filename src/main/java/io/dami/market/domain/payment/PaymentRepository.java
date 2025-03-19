package io.dami.market.domain.payment;

import java.util.Optional;

public interface PaymentRepository {

  Payment save(Payment payment);

  Optional<Payment> duplicateCheck(Long orderId);

}
