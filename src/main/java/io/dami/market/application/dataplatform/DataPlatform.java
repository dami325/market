package io.dami.market.application.dataplatform;

import io.dami.market.domain.order.Order;
import io.dami.market.domain.payment.Payment;

public interface DataPlatform {
    void publish(Order order, Payment payment);
}
