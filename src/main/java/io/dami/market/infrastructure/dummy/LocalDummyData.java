package io.dami.market.infrastructure.dummy;

import io.dami.market.domain.coupon.Coupon;
import io.dami.market.domain.coupon.CouponRepository;
import io.dami.market.domain.order.Order;
import io.dami.market.domain.order.Order.OrderStatus;
import io.dami.market.domain.order.OrderDetail;
import io.dami.market.domain.order.OrderRepository;
import io.dami.market.domain.point.Point;
import io.dami.market.domain.point.PointRepository;
import io.dami.market.domain.product.Product;
import io.dami.market.domain.product.ProductRepository;
import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
  private final PointRepository pointRepository;
  private final ProductRepository productRepository;
  private final UserRepository userRepository;

  @Value("${dummy-data}")
  private boolean initDummy;

  @Transactional
  public void init() {
    if (initDummy) {


      couponRepository.save(Coupon.builder()
          .name("선착순쿠폰1")
          .endDate(LocalDateTime.now().plusDays(5))
          .issuedQuantity(0)
          .discountAmount(new BigDecimal("1000"))
          .totalQuantity(500000)
          .build());

      couponRepository.save(Coupon.builder()
          .name("선착순쿠폰2")
          .endDate(LocalDateTime.now().plusDays(5))
          .issuedQuantity(0)
          .discountAmount(new BigDecimal("1000"))
          .totalQuantity(500000)
          .build());

      for (int i = 0; i < 20; i++) {
        productRepository.save(Product.builder()
            .name("product " + i)
            .price(new BigDecimal("1000"))
            .stockQuantity(100000)
            .build());
      }

      int userCount = 10;
      for (int i = 1; i <= userCount; i++) {
        User user = userRepository.save(User.builder()
            .username("tester" + i)
            .build());

        pointRepository.save(Point.builder()
            .totalPoint(new BigDecimal("1000000"))
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
            .status(OrderStatus.ORDER_COMPLETE)
            .build());

        int count = 0;

        long orderProductCount = 10L;
        for (long j = 1L; j < orderProductCount; j++) {
          Long productIndex = j;
          if (count == 5) {
            break;
          }
          count++;
          order.addOrderDetail(
              OrderDetail.createOrderDetail(order, productIndex, 10,
                  new BigDecimal("3000")));
        }
      }
    }
  }
}
