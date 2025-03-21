package io.dami.market.domain.product;

import io.dami.market.interfaces.product.ProductResponse;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {

  Product getProduct(Long productId);

  List<Product> getProducts(Pageable pageable);

  Long getProductsCount();

  List<ProductResult.Top5ProductDetails> getProductsTop5();

  List<Product> findAllByIdWithLock(Set<Long> orderProductIds);

  Product save(Product productA);

  List<Product> getAllById(Set<Long> productIds);

  List<Product> saveAll(List<Product> products);
}
