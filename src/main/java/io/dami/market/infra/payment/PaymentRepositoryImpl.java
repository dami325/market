package io.dami.market.infra.payment;

import io.dami.market.domain.payment.Payment;
import io.dami.market.domain.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }

    @Override
    public Optional<Payment> duplicateCheck(Long orderId) {
        return paymentJpaRepository.duplicateCheck(orderId);
    }

}
