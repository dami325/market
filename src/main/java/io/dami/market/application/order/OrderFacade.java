package io.dami.market.application.order;

import io.dami.market.domain.coupon.CouponService;
import io.dami.market.domain.order.Order;
import io.dami.market.domain.order.OrderCommand;
import io.dami.market.domain.order.OrderService;
import io.dami.market.domain.product.Product;
import io.dami.market.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;
    private final CouponService couponService;
    private final ProductService productService;

    @Transactional
    public Order createOrder(OrderCommand.CreateOrder commend) {
        Map<Long, Product> productMap = productService.getProductMap(commend.productIds());
        BigDecimal discountAmount = couponService.useCoupon(commend.issuedCouponId());
        return orderService.createOrder(commend, discountAmount, productMap);
    }
}
