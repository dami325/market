package io.dami.market.infrastructure.product;

import static io.dami.market.domain.order.QOrder.order;
import static io.dami.market.domain.order.QOrderDetail.orderDetail;
import static io.dami.market.domain.product.QProduct.product;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.dami.market.domain.order.Order;
import io.dami.market.domain.product.Product;
import io.dami.market.domain.product.ProductResult;
import io.dami.market.domain.product.QProductResult_Top5ProductDetails;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductQuerydslRepository {

  private final JPAQueryFactory queryFactory;

  public List<Product> getProducts(Pageable pageable) {
    return queryFactory
        .selectFrom(product)
        .limit(pageable.getPageSize())
        .offset(pageable.getOffset())
        .orderBy(product.id.desc())
        .fetch();
  }

  public Long getProductsCount() {
    return queryFactory
        .select(product.id.count())
        .from(product)
        .fetchOne();
  }

  public List<ProductResult.Top5ProductDetails> getProductsTop5() {
    return queryFactory
        .select(new QProductResult_Top5ProductDetails(
            product.id,
            product.name,
            product.price,
            product.stockQuantity,
            orderDetail.quantity.sum()
        ))
        .from(orderDetail)
        .innerJoin(orderDetail.order, order)
        .innerJoin(product)
        .on(
            product.id.eq(orderDetail.productId)
        )
        .where(
            order.status.eq(Order.OrderStatus.ORDER_COMPLETE),
            order.createdAt.between(LocalDateTime.now().minusDays(3), LocalDateTime.now())
        )
        .groupBy(
            product.id,
            product.name,
            product.price,
            product.stockQuantity
        )
        .orderBy(orderDetail.quantity.sum().desc())
        .limit(5)
        .fetch();
  }

}
