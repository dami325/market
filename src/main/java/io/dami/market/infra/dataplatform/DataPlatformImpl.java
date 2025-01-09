package io.dami.market.infra.dataplatform;

import io.dami.market.domain.order.DataPlatform;
import io.dami.market.domain.order.Order;
import io.dami.market.domain.payment.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 데이터 플랫폼
 */
@Slf4j
@Component
public class DataPlatformImpl implements DataPlatform {

    // 주문 정보 전송
    public void publish(Order order, Payment payment) {
        log.info("Publishing to Mock Data Platform. Order ID: {}, Payment ID: {}", order.getId(), payment.getId());
    }
}
