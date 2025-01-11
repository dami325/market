package io.dami.market.infra.product;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.dami.market.domain.order.Order;
import io.dami.market.domain.product.Product;
import io.dami.market.interfaces.product.ProductResponse;
import io.dami.market.interfaces.product.QProductResponse_Top5ProductDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.dami.market.domain.order.QOrder.order;
import static io.dami.market.domain.order.QOrderDetail.orderDetail;
import static io.dami.market.domain.product.QProduct.product;

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

    public List<ProductResponse.Top5ProductDetails> getProductsTop5() {
        return queryFactory
                .select(new QProductResponse_Top5ProductDetails(
                        product.id,
                        product.name,
                        product.price,
                        product.stockQuantity,
                        orderDetail.quantity.sum()
                ))
                .from(orderDetail)
                .innerJoin(orderDetail.order, order)
                .innerJoin(orderDetail.product, product)
                .where(
                        order.status.eq(Order.OrderStatus.PAYMENT_SUCCESS)
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
