package io.dami.market.infra.dataplatform;

import io.dami.market.domain.order.Order;
import io.dami.market.domain.payment.Payment;
import org.springframework.stereotype.Component;

/**
 * 데이터 플랫폼
 */
@Component
public interface DataPlatform {

    // 주문 정보 전송
    void publish(Order order, Payment payment);

}
