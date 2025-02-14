package io.dami.market.infra.dummy;

import io.dami.market.domain.coupon.Coupon;
import io.dami.market.domain.coupon.CouponRepository;
import io.dami.market.domain.order.Order;
import io.dami.market.domain.order.Order.OrderStatus;
import io.dami.market.domain.order.OrderDetail;
import io.dami.market.domain.order.OrderRepository;
import io.dami.market.domain.payment.PaymentRepository;
import io.dami.market.domain.point.Point;
import io.dami.market.domain.point.PointRepository;
import io.dami.market.domain.product.Product;
import io.dami.market.domain.product.ProductRepository;
import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("local")
@RequiredArgsConstructor
public class LocalDummyData {

  private final CouponRepository couponRepository;
  private final OrderRepository orderRepository;
  private final PaymentRepository paymentRepository;
  private final PointRepository pointRepository;
  private final ProductRepository productRepository;
  private final UserRepository userRepository;

  @Value("${dummy-data}")
  private boolean initDummy;

  @Transactional
  public void init() {
    if (initDummy) {

      int userCount = 500;

      couponRepository.save(Coupon.builder()
          .name("선착순쿠폰")
          .endDate(LocalDateTime.now().plusDays(5))
          .issuedQuantity(0)
          .discountAmount(new BigDecimal("1000"))
          .totalQuantity(500)
          .build());

      for (int i = 0; i < 100000; i++) {
        productRepository.save(Product.builder()
            .name("product " + i)
            .price(new BigDecimal("1000"))
            .stockQuantity(1000)
            .build());
      }

      List<Product> products = new ArrayList<>();
      for (Long i = 1L; i <= 100000; i++) {
        Product product = productRepository.getProduct(i);
        products.add(product);
      }

      for (int i = 1; i <= userCount; i++) {
        User user = userRepository.save(User.builder()
            .username("tester" + i)
            .build());

        pointRepository.save(Point.builder()
            .totalPoint(new BigDecimal("100000"))
            .userId(user.getId())
            .build());

        Coupon coupon = couponRepository.save(Coupon.builder()
            .name("coupon" + i)
            .endDate(LocalDateTime.now().plusDays(5))
            .issuedQuantity(0)
            .discountAmount(new BigDecimal("1000"))
            .totalQuantity(100)
            .build());

        coupon.issuedCoupon(user.getId());

        Order order = orderRepository.save(Order.builder()
            .userId(user.getId())
            .issuedCouponId(null)
            .status(
                user.getId() % 2 == 0 ? OrderStatus.ORDER_COMPLETE : OrderStatus.ORDER_CANCELLED)
            .build());

        int count = 0;
        while (true) {
          Long productIndex = (long) (Math.random() * 100000);
          if (count == 5) {
            break;
          }
          count++;
          order.addOrderDetail(
              OrderDetail.createOrderDetail(order, productIndex, (int) (Math.random() * 1001),
                  new BigDecimal("3000")));
        }
      }
    }
  }

}
