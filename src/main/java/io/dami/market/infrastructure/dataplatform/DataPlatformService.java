package io.dami.market.infrastructure.dataplatform;

import io.dami.market.application.payment.event.PayCompleteEvent;
import org.springframework.stereotype.Component;

/**
 * 데이터 플랫폼
 */
@Component
public interface DataPlatformService {

  // 주문 정보 전송
  void sendOrderPaymentData(PayCompleteEvent event);

}
