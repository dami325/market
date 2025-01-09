package io.dami.market.application.product;

import io.dami.market.application.order.OrderFacade;
import io.dami.market.domain.coupon.Coupon;
import io.dami.market.domain.coupon.CouponRepository;
import io.dami.market.domain.order.OrderCommand;
import io.dami.market.domain.product.Product;
import io.dami.market.domain.product.ProductRepository;
import io.dami.market.domain.user.User;
import io.dami.market.domain.user.UserRepository;
import io.dami.market.interfaces.product.ProductResponse;
import io.dami.market.utils.IntegrationServiceTest;
import io.dami.market.utils.fixture.CouponFixture;
import io.dami.market.utils.fixture.ProductFixture;
import io.dami.market.utils.fixture.UserFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

class ProductServiceIntegrationTest extends IntegrationServiceTest {

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserRepository userRepository;

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
        User user = UserFixture.user("박주닮",10000000);
        userRepository.save(user);
        int maxQuantity = 50;
        int minQuantity = 2;
        List<OrderCommand.Product> products = List.of(
                new OrderCommand.Product(productA.getId(), minQuantity),
                new OrderCommand.Product(productB.getId(), 20),
                new OrderCommand.Product(productC.getId(), 35),
                new OrderCommand.Product(productD.getId(), 45),
                new OrderCommand.Product(productE.getId(), maxQuantity),
                new OrderCommand.Product(productF.getId(), 1)
        );
        OrderCommand.DoOrder command = new OrderCommand.DoOrder(user.getId(), null, products);
        orderFacade.createOrder(command);

        // when
        List<ProductResponse.Top5ProductDetails> result = productService.getProductsTop5();

        // then
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.size()).isEqualTo(5);
        int maxSaleCount = 0;
        int minSaleCount = Integer.MAX_VALUE;
        for (ProductResponse.Top5ProductDetails productDetails : result) {
            maxSaleCount = Math.max(maxSaleCount,productDetails.total_quantity_sold());
            minSaleCount = Math.min(minSaleCount,productDetails.total_quantity_sold());
        }
        Assertions.assertThat(maxSaleCount).isEqualTo(maxQuantity);
        Assertions.assertThat(minSaleCount).isEqualTo(minQuantity);
    }
}
