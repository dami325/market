package io.dami.market.domain.product;

import io.dami.market.interfaces.product.ProductResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProductRepository {

    Product getProduct(Long productId);

    List<Product> getProducts(Pageable pageable);

    Long getProductsCount();

    List<ProductResponse.Top5ProductDetails> getProductsTop5();

    Map<Long, Product> findAllByIdWithLock(List<Long> orderProductIds);

    Product save(Product productA);

    List<Product> getAllById(Set<Long> productIds);

    List<Product> saveAll(List<Product> products);
}
