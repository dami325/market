package io.dami.market.domain.payment;

import io.dami.market.domain.coupon.Coupon;
import io.dami.market.domain.coupon.CouponRepository;
import io.dami.market.domain.coupon.IssuedCoupon;
import io.dami.market.domain.order.Order;
import io.dami.market.domain.order.OrderRepository;
import io.dami.market.domain.product.Product;
import io.dami.market.domain.product.ProductRepository;
import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserRepository;
import io.dami.market.infra.coupon.IssuedCouponJpaRepository;
import io.dami.market.utils.IntegrationServiceTest;
import io.dami.market.utils.fixture.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

class PaymentServiceIntegrationTest extends IntegrationServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private IssuedCouponJpaRepository issuedCouponJpaRepository;




}
