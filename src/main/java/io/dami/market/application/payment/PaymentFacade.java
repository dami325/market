package io.dami.market.application.payment;

import io.dami.market.domain.coupon.CouponService;
import io.dami.market.domain.order.Order;
import io.dami.market.domain.order.OrderService;
import io.dami.market.domain.payment.Payment;
import io.dami.market.domain.payment.PaymentService;
import io.dami.market.domain.point.PointService;
import io.dami.market.domain.product.ProductService;
import io.dami.market.domain.user.UserCoupon;
import io.dami.market.infra.dataplatform.DataPlatform;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

    private final PaymentService paymentService;
    private final CouponService couponService;
    private final ProductService productService;
    private final PointService pointService;
    private final OrderService orderService;
    private final DataPlatform dataPlatform;

    @Transactional
    public void pay(Long userId, Long orderId, Long userCouponId) {
        // 주문 조회
        Order order = orderService.getOrderWithLock(orderId);

        // 쿠폰 조회 nullable
        UserCoupon userCoupon = couponService.getUserCouponOrNull(userCouponId);

        // 결제 테이블 생성
        Payment payment = paymentService.pay(order, userCoupon);

        // 상품 재고 검증 및 차감
        productService.quantitySubtract(order);

        // 포인트 검증 및 차감
        pointService.usePoints(userId, payment.getAmount());

        // 외부 플랫폼으로 주문정보 전송
        dataPlatform.publish(order, payment);
    }
}
