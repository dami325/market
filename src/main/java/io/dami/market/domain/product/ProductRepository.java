package io.dami.market.domain.product;

import io.dami.market.interfaces.product.ProductResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

public interface ProductRepository {

    Product getProduct(Long productId);

    List<Product> getProducts(Pageable pageable);

    Long getProductsCount();

    List<ProductResponse.Top5ProductDetails> getProductsTop5();

    void findAllByIdWithLock(List<Long> orderProductIds);

    Product save(Product productA);
}
