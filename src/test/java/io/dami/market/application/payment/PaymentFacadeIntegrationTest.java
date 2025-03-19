package io.dami.market.application.payment;

import io.dami.market.application.order.OrderFacade;
import io.dami.market.domain.coupon.Coupon;
import io.dami.market.domain.coupon.CouponRepository;
import io.dami.market.domain.coupon.IssuedCoupon;
import io.dami.market.domain.order.Order;
import io.dami.market.domain.order.OrderCommand;
import io.dami.market.domain.order.OrderDetail;
import io.dami.market.domain.order.OrderRepository;
import io.dami.market.domain.order.PaymentAlreadySuccessException;
import io.dami.market.domain.payment.Payment;
import io.dami.market.domain.point.Point;
import io.dami.market.domain.point.PointRepository;
import io.dami.market.domain.product.Product;
import io.dami.market.domain.product.ProductIsOutOfStock;
import io.dami.market.domain.product.ProductRepository;
import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserRepository;
import io.dami.market.infrastructure.coupon.IssuedCouponJpaRepository;
import io.dami.market.infrastructure.product.ProductJpaRepository;
import io.dami.market.utils.IntegrationServiceTest;
import io.dami.market.utils.fixture.CouponFixture;
import io.dami.market.utils.fixture.PointFixture;
import io.dami.market.utils.fixture.ProductFixture;
import io.dami.market.utils.fixture.UserCouponFixture;
import io.dami.market.utils.fixture.UserFixture;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PaymentFacadeIntegrationTest extends IntegrationServiceTest {

  @Autowired
  private OrderFacade orderFacade;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ProductJpaRepository productJpaRepository;

  @Autowired
  private CouponRepository couponRepository;

  @Autowired
  private IssuedCouponJpaRepository issuedCouponJpaRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private PaymentFacade paymentFacade;

  @Autowired
  private PointRepository pointRepository;

  @Autowired
  private OrderRepository orderRepository;

  @Test
  void 결제_주문상태_할인적용_결제금액_결제상태_검증_성공() {
    // given
    Coupon coupon = couponRepository.save(CouponFixture.coupon("새해쿠폰", new BigDecimal("10000")));
    Product productA = productRepository.save(ProductFixture.product("productA", 10000));
    Product productB = productRepository.save(ProductFixture.product("productB", 20000));
    User user = userRepository.save(UserFixture.user("박주닮"));
    pointRepository.save(PointFixture.point(user.getId(), 90000));
    IssuedCoupon issuedCoupon = issuedCouponJpaRepository.save(
        UserCouponFixture.userCoupon(coupon, user.getId()));
    int orderQuantity = 3;

    List<OrderCommand.ProductStock> productStocks = List.of(
        new OrderCommand.ProductStock(productA.getId(), orderQuantity), // 3만원
        new OrderCommand.ProductStock(productB.getId(), orderQuantity) // 6만원
    );
    OrderCommand.CreateOrder commend = new OrderCommand.CreateOrder(user.getId(),
        issuedCoupon.getId(), productStocks);
    Order order = orderFacade.createOrder(commend);

    // when
    paymentFacade.processOrderPayment(user.getId(), order.getId());

    // then 테스트 에서만 쓰는 쿼리 만들기 싫어서 EntityManager 로 구현
    Order resultOrder = (Order) em.createQuery(
            "select orders from Order orders where orders.userId = :userid")
        .setParameter("userid", user.getId())
        .getSingleResult();

    Payment resultPayment = (Payment) em.createQuery(
            "select pay from Payment pay where pay.orderId = :orderId")
        .setParameter("orderId", resultOrder.getId())
        .getSingleResult();

    // 외부 데이터 플랫폼 전송은 Publishing to Mock Data Platform. Order ID: 1, Payment ID: 1 로그로 확인
    Assertions.assertThat(resultOrder.getStatus())
        .isEqualTo(Order.OrderStatus.ORDER_COMPLETE); // 주문 상태 검증
    Assertions.assertThat(resultOrder.getDiscountAmount().compareTo(coupon.getDiscountAmount()))
        .isEqualTo(0); // 할인적용 검증
    Assertions.assertThat(resultPayment.getPaymentStatus())
        .isEqualTo(Payment.PaymentStatue.SUCCESS); // 결제 상태 검증
    Assertions.assertThat(resultPayment.getTotalAmount())
        .isEqualTo(resultOrder.getTotalAmount()); // 결제금액 검증
  }

  @Test
  void 주문_결제_재고없음_실패() {
    // given
    Coupon coupon = couponRepository.save(CouponFixture.coupon("새해쿠폰", new BigDecimal("10000")));
    Product productA = productRepository.save(ProductFixture.product("productA", 10000, 1));
    Product productB = productRepository.save(ProductFixture.product("productB", 20000, 2));
    User user = userRepository.save(UserFixture.user("박주닮"));
    pointRepository.save(PointFixture.point(user.getId(), 90000));
    IssuedCoupon issuedCoupon = issuedCouponJpaRepository.save(
        UserCouponFixture.userCoupon(coupon, user.getId()));
    int orderQuantity = 3;

    List<OrderCommand.ProductStock> productStocks = List.of(
        new OrderCommand.ProductStock(productA.getId(), orderQuantity),
        new OrderCommand.ProductStock(productB.getId(), orderQuantity)
    );
    OrderCommand.CreateOrder commend = new OrderCommand.CreateOrder(user.getId(),
        issuedCoupon.getId(), productStocks);
    Order order = orderFacade.createOrder(commend);

    // when & then
    Assertions.assertThatThrownBy(
            () -> paymentFacade.processOrderPayment(user.getId(), order.getId()), "결제 재고 없어서 실패")
        .isInstanceOf(ProductIsOutOfStock.class)
        .hasMessageContaining("재고가 부족합니다");

  }

  @Test
  void 주문_결제_포인트_없음_실패() {
    // given
    Coupon coupon = couponRepository.save(CouponFixture.coupon("새해쿠폰", new BigDecimal("10000")));
    Product productA = productRepository.save(ProductFixture.product("productA", 10000));
    Product productB = productRepository.save(ProductFixture.product("productB", 20000));
    User user = userRepository.save(UserFixture.user("박주닮"));
    pointRepository.save(PointFixture.point(user.getId(), 10000));
    IssuedCoupon issuedCoupon = issuedCouponJpaRepository.save(
        UserCouponFixture.userCoupon(coupon, user.getId()));
    int orderQuantity = 3;

    List<OrderCommand.ProductStock> productStocks = List.of(
        new OrderCommand.ProductStock(productA.getId(), orderQuantity), // 3만원
        new OrderCommand.ProductStock(productB.getId(), orderQuantity) // 6만원
    );
    OrderCommand.CreateOrder commend = new OrderCommand.CreateOrder(user.getId(),
        issuedCoupon.getId(), productStocks);
    Order order = orderFacade.createOrder(commend);

    // when & then
    Assertions.assertThatThrownBy(
            () -> paymentFacade.processOrderPayment(user.getId(), order.getId()), "포인트 부족해서 실패")
        .hasMessageContaining("포인트 부족 사용 실패");
  }

  @DisplayName("""
      결제 동시성 테스트 한 유저가 한 주문에 대해 중복 결제 요청 시 
      이미 결제가 완료된 주문 에러 발생 
      하나의 요청만 결제됨
      """)
  @Test
  void 결제_동시성_테스트1() throws InterruptedException {
    // given
    Coupon coupon = couponRepository.save(CouponFixture.coupon("새해쿠폰", new BigDecimal("10000")));
    Product productA = productRepository.save(ProductFixture.product("productA", 10000));
    Product productB = productRepository.save(ProductFixture.product("productB", 20000));
    User user = userRepository.save(UserFixture.user("박주닮"));
    IssuedCoupon issuedCoupon = issuedCouponJpaRepository.save(
        UserCouponFixture.userCoupon(coupon, user.getId()));
    BigDecimal beforePoint = pointRepository.save(PointFixture.point(user.getId(), 90000))
        .getTotalPoint();

    int orderQuantity = 3;

    List<OrderCommand.ProductStock> productStocks = List.of(
        new OrderCommand.ProductStock(productA.getId(), orderQuantity), // 3만원
        new OrderCommand.ProductStock(productB.getId(), orderQuantity) // 6만원
    );
    OrderCommand.CreateOrder commend = new OrderCommand.CreateOrder(user.getId(),
        issuedCoupon.getId(), productStocks);
    Order order = orderFacade.createOrder(commend);

    int threads = 3;
    ExecutorService executorService = Executors.newFixedThreadPool(threads);
    CountDownLatch latch = new CountDownLatch(threads);

    // when
    for (int i = 0; i < threads; i++) {
      executorService.submit(() -> {
        try {
          paymentFacade.processOrderPayment(user.getId(), order.getId());
        } catch (PaymentAlreadySuccessException e) {
          Assertions.assertThat(e.getMessage()).isEqualTo("이미 결제가 완료된 주문입니다.");
        } finally {
          latch.countDown();
        }
      });
    }
    latch.await();
    executorService.shutdown();

    // then
    Order resultOrder = (Order) em.createQuery(
            "select orders from Order orders where orders.userId = :userid")
        .setParameter("userid", user.getId())
        .getSingleResult();

    Payment resultPayment = (Payment) em.createQuery(
            "select pay from Payment pay where pay.orderId = :orderId")
        .setParameter("orderId", resultOrder.getId())
        .getSingleResult();

    Point afterPoint = pointRepository.getPointByUserId(user.getId());
    BigDecimal resultPoint = afterPoint.getTotalPoint();
    Assertions.assertThat(resultPoint)
        .isEqualTo(beforePoint.subtract(resultPayment.getTotalAmount())); // 사용자 금액 차감 검증
    Assertions.assertThat(resultOrder.getStatus())
        .isEqualTo(Order.OrderStatus.ORDER_COMPLETE); // 주문 상태 검증
    Assertions.assertThat(resultOrder.getDiscountAmount().compareTo(coupon.getDiscountAmount()))
        .isEqualTo(0); // 할인적용 검증
    Assertions.assertThat(resultPayment.getPaymentStatus())
        .isEqualTo(Payment.PaymentStatue.SUCCESS); // 결제 상태 검증
    Assertions.assertThat(resultPayment.getTotalAmount())
        .isEqualTo(resultOrder.getTotalAmount()); // 결제금액 검증
  }


  @DisplayName("100명 동시 결제 시 상품별 재고 1000개 차감 검증")
  @Test
  void 동시_100명_결제_재고_검증() throws InterruptedException {
    // given
    int threads = 100;
    int productCount = 3;

    List<Product> products = new ArrayList<>();
    Map<User, Order> userOrderMap = new HashMap<>();

    for (int i = 0; i < productCount; i++) {
      Product product = productRepository.save(Product.builder()
          .name("product " + i)
          .price(new BigDecimal("1000"))
          .stockQuantity(1000)
          .build());
      products.add(product);
    }

    for (int i = 1; i <= threads; i++) {
      User user = userRepository.save(User.builder()
          .username("tester" + i)
          .build());

      pointRepository.save(Point.builder()
          .totalPoint(new BigDecimal("100000"))
          .userId(user.getId())
          .build());

      Order order = Order.createOrderForm(user.getId(), null);
      for (Product product : products) {

        OrderDetail orderDetail = OrderDetail.createOrderDetail(order, product.getId(), 10,
            new BigDecimal("3000"));

        order.addOrderDetail(orderDetail);
      }
      orderRepository.save(order);
      userOrderMap.put(user, order);
    }

    ExecutorService executorService = Executors.newFixedThreadPool(threads);
    CountDownLatch latch = new CountDownLatch(threads);

    // when
    for (Map.Entry<User, Order> entry : userOrderMap.entrySet()) {
      executorService.submit(() -> {
        try {
          User user = entry.getKey();
          Order order = entry.getValue();
          paymentFacade.processOrderPayment(user.getId(), order.getId());
        } finally {
          latch.countDown();
        }
      });
    }
    latch.await();
    executorService.shutdown();

    // then
    Set<Long> productIds = products.stream().map(Product::getId).collect(Collectors.toSet());
    List<Product> result = productJpaRepository.findAllById(productIds);
    for (Product product : result) {
      Assertions.assertThat(product.getStockQuantity()).isEqualTo(0);
    }
  }
}
