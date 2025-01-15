package io.dami.market.infra.product;

import io.dami.market.domain.product.Product;
import io.dami.market.domain.product.ProductRepository;
import io.dami.market.interfaces.product.ProductResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;
    private final ProductQuerydslRepository querydslRepository;

    @Override
    public Product getProduct(Long productId) {
        return productJpaRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("유효하지않은 상품"));
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
    public List<Product> findAllByIdWithLock(List<Long> orderProductIds) {
        return productJpaRepository.findAllByIdWithLock(orderProductIds);
    }

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public List<Product> getAllById(Set<Long> productIds) {
        List<Product> products = productJpaRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new EntityNotFoundException("일부 상품이 존재하지 않습니다.");
        }
        return products;
    }

}
