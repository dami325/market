package io.dami.market.domain.order;

import io.dami.market.domain.payment.Payment;

public interface DataPlatform {
    void publish(Order order, Payment payment);
}
