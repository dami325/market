package io.dami.market.application.payment;

import io.dami.market.domain.order.Order;
import io.dami.market.domain.order.OrderRepository;
import io.dami.market.domain.payment.Payment;
import io.dami.market.domain.payment.PaymentRepository;
import io.dami.market.domain.user.UserCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public Payment pay(Order order, UserCoupon userCoupon) {
        Payment payment = paymentRepository.save(new Payment(order, userCoupon));
        payment.pay();
        return payment;
    }
}
