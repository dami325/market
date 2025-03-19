package io.dami.market.domain.payment;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

  private final PaymentRepository paymentRepository;

  @Transactional
  public Payment pay(Long orderId, BigDecimal totalAmount) {
    paymentRepository.duplicateCheck(orderId).ifPresent(Payment::paymentValidate);
    Payment payment = paymentRepository.save(Payment.createPaymentForm(orderId, totalAmount));
    payment.success();
    return payment;
  }
}
