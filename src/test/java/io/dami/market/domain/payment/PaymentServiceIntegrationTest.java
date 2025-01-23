package io.dami.market.domain.payment;

import io.dami.market.domain.coupon.CouponRepository;
import io.dami.market.domain.order.OrderRepository;
import io.dami.market.domain.product.ProductRepository;
import io.dami.market.domain.user.UserRepository;
import io.dami.market.infra.coupon.IssuedCouponJpaRepository;
import io.dami.market.utils.IntegrationServiceTest;
import org.springframework.beans.factory.annotation.Autowired;

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
