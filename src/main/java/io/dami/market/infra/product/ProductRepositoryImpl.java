package io.dami.market.infra.product;

import io.dami.market.domain.product.Product;
import io.dami.market.domain.product.ProductRepository;
import io.dami.market.interfaces.product.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;
    private final ProductQuerydslRepository querydslRepository;

    @Override
    public Product getProduct(Long productId) {
        return productJpaRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지않은 상품"));
    }

    @Override
    public List<Product> getProducts(Pageable pageable) {
        return querydslRepository.getProducts(pageable);
    }

    @Override
    public Long getProductsCount() {
        return querydslRepository.getProductsCount();
    }

    @Override
    public List<ProductResponse.Top5ProductDetails> getProductsTop5() {
        return querydslRepository.getProductsTop5();
    }

    @Override
    public void findAllByIdWithLock(List<Long> orderProductIds) {
        productJpaRepository.findAllByIdWithLock(orderProductIds);
    }

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }
}
