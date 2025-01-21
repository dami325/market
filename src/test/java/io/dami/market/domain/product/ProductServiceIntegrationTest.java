package io.dami.market.domain.product;

import io.dami.market.application.order.OrderFacade;
import io.dami.market.application.payment.PaymentFacade;
import io.dami.market.domain.order.Order;
import io.dami.market.domain.order.OrderCommand;
import io.dami.market.domain.order.OrderRepository;
import io.dami.market.domain.order.OrderService;
import io.dami.market.domain.point.Point;
import io.dami.market.domain.point.PointRepository;
import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserRepository;
import io.dami.market.infra.product.ProductJpaRepository;
import io.dami.market.interfaces.product.ProductResponse;
import io.dami.market.utils.IntegrationServiceTest;
import io.dami.market.utils.fixture.OrderFixture;
import io.dami.market.utils.fixture.PointFixture;
import io.dami.market.utils.fixture.ProductFixture;
import io.dami.market.utils.fixture.UserFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ProductServiceIntegrationTest extends IntegrationServiceTest {

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentFacade paymentFacade;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private PointRepository pointRepository;

    @DisplayName("상품 재고 차감 10명 동시성 테스트 성공")
    @Test
    void 상품_재고_차감_10명_동시성_테스트_성공() throws InterruptedException {
        // given
        int stockQuantity = 30; // 재고 수량 세팅
        int quantity = 3; // 3개 주문
        Product product = productRepository.save(ProductFixture.product("좋은데이", 5000, stockQuantity));
        User user = userRepository.save(UserFixture.user("박주닮"));
        Order order = orderRepository.save(OrderFixture.order(user.getId(), quantity, product));

        int threads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        // when
        for (int i = 0; i < threads; i++) {
            executorService.submit(() -> {
                try {
                    productService.quantitySubtract(order.getProductQuantityMap());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        // then
        Product result = productJpaRepository.findById(product.getId()).get();
        Assertions.assertThat(result.getStockQuantity()).isEqualTo(0);
    }


    @Test
    void 상품_리스트_조회_성공() {
        // given
        productRepository.save(ProductFixture.product("좋은데이"));
        productRepository.save(ProductFixture.product("진로"));
        productRepository.save(ProductFixture.product("참이슬"));

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<ProductResponse.ProductDetails> result = productService.getProducts(pageable);

        // then
        Assertions.assertThat(result.getContent().size()).isEqualTo(3);
    }

    @Test
    void 상품_상세_조회_성공() {
        // given
        Product product = productRepository.save(ProductFixture.product("좋은데이"));

        // when
        Product result = productService.getProductDetails(product.getId());

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getName()).isEqualTo("좋은데이");
        Assertions.assertThat(result.getStockQuantity()).isEqualTo(100);
    }

    @Test
    void 탑5_상품_조회_성공() {
        // given
        Product productA = productRepository.save(ProductFixture.product("진로", 10000));
        Product productB = productRepository.save(ProductFixture.product("좋은데이", 20000));
        Product productC = productRepository.save(ProductFixture.product("참이슬", 20000));
        Product productD = productRepository.save(ProductFixture.product("카스", 20000));
        Product productE = productRepository.save(ProductFixture.product("처음처럼", 20000));
        Product productF = productRepository.save(ProductFixture.product("테라", 20000));
        User user = userRepository.save(UserFixture.user("박주닮"));
        Point point = pointRepository.save(PointFixture.point(user.getId(), 10000000));
        int maxQuantity = 50;
        int minQuantity = 2;
        List<OrderCommand.ProductStock> productStocks = List.of(
                new OrderCommand.ProductStock(productA.getId(), minQuantity),
                new OrderCommand.ProductStock(productB.getId(), 20),
                new OrderCommand.ProductStock(productC.getId(), 35),
                new OrderCommand.ProductStock(productD.getId(), 45),
                new OrderCommand.ProductStock(productE.getId(), maxQuantity),
                new OrderCommand.ProductStock(productF.getId(), 1)
        );
        OrderCommand.CreateOrder commend = new OrderCommand.CreateOrder(user.getId(), null, productStocks);
        Order order = orderFacade.createOrder(commend);
        paymentFacade.processOrderPayment(user.getId(), order.getId());

        // when
        List<ProductResponse.Top5ProductDetails> result = productService.getProductsTop5();

        // then
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.size()).isEqualTo(5);
        int maxSaleCount = 0;
        int minSaleCount = Integer.MAX_VALUE;
        for (ProductResponse.Top5ProductDetails productDetails : result) {
            maxSaleCount = Math.max(maxSaleCount, productDetails.total_quantity_sold());
            minSaleCount = Math.min(minSaleCount, productDetails.total_quantity_sold());
        }
        Assertions.assertThat(maxSaleCount).isEqualTo(maxQuantity);
        Assertions.assertThat(minSaleCount).isEqualTo(minQuantity);
    }

    @DisplayName("주문 상품 재고 부족 차감 실패 ProductIsOutOfStock 발생")
    @Test
    void 상품_재고_부족_차감_실패() {
        // given
        int stockQuantity = 2; // 재고 수량 세팅
        int quantity = 3; // 3개 주문
        Product product = productRepository.save(ProductFixture.product("좋은데이", 5000, stockQuantity));
        User user = userRepository.save(UserFixture.user("박주닮"));
        Order order = orderRepository.save(OrderFixture.order(user.getId(), quantity, product));

        // when & then
        Assertions.assertThatThrownBy(() -> productService.quantitySubtract(order.getProductQuantityMap()))
                .isInstanceOf(ProductIsOutOfStock.class);
    }
}
