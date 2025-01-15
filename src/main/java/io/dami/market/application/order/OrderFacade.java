package io.dami.market.application.order;

import io.dami.market.application.coupon.CouponService;
import io.dami.market.application.payment.PaymentService;
import io.dami.market.application.point.PointService;
import io.dami.market.application.product.ProductService;
import io.dami.market.domain.order.Order;
import io.dami.market.domain.order.OrderCommand;
import io.dami.market.domain.order.OrderService;
import io.dami.market.domain.payment.Payment;
import io.dami.market.domain.user.UserCoupon;
import io.dami.market.infra.dataplatform.DataPlatform;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final CouponService couponService;
    private final PointService pointService;
    private final ProductService productService;
    private final DataPlatform dataPlatform; // 데이터 플랫폼 전송 역할

    @Transactional
    public void createOrder(OrderCommand.order command) {
        // 주문서 생성
        Order order = orderService.order(command.userId(), command.orderDetails());

        // 쿠폰 조회 nullable
        UserCoupon userCoupon = couponService.getUserCouponOrNull(command.userCouponId());

        // 결제 테이블 생성
        Payment payment = paymentService.pay(order, userCoupon);

        // 상품 재고 검증 및 차감
        productService.quantitySubtract(order);

        // 포인트 검증 및 차감
        pointService.usePoints(command.userId(), payment.getAmount());

        // 외부 플랫폼으로 주문정보 전송
        dataPlatform.publish(order, payment);
    }
}
