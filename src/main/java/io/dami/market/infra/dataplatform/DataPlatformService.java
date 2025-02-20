package io.dami.market.infra.dataplatform;

import io.dami.market.domain.payment.event.PayCompleteEvent;
import org.springframework.stereotype.Component;

/**
 * 데이터 플랫폼
 */
@Component
public interface DataPlatformService {

  // 주문 정보 전송
  void sendOrderPaymentData(PayCompleteEvent event);

}
