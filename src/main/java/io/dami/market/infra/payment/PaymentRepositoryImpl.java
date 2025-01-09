package io.dami.market.infra.payment;

import io.dami.market.domain.payment.Payment;
import io.dami.market.domain.payment.PaymentRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    @Override
    public Payment save(Payment payment) {
        return null;
    }
}
