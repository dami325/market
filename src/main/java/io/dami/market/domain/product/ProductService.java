package io.dami.market.domain.product;

import io.dami.market.interfaces.product.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


    @Transactional(readOnly = true)
    public Page<ProductResponse.ProductDetails> getProducts(Pageable pageable) {

        List<ProductResponse.ProductDetails> content = productRepository.getProducts(pageable).stream()
                .map(ProductResponse.ProductDetails::new)
                .toList();

        Long total = productRepository.getProductsCount();

        return new PageImpl<>(content, pageable, total);
    }

    @Transactional(readOnly = true)
    public Product getProductDetails(Long productId) {
        return productRepository.getProduct(productId);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse.Top5ProductDetails> getProductsTop5() {
        return productRepository.getProductsTop5();
    }

    @Transactional
    public void quantitySubtract(Map<Long, Integer> productQuantityMap) {
        Map<Long, Product> productMap = productRepository.findAllByIdWithLock(productQuantityMap.keySet()).stream()
                .collect(toMap(Product::getId, identity()));

        productQuantityMap.forEach((productId, quantity) -> {
            Product product = productMap.get(productId);
            product.subtractStock(quantity);
        });
    }

    @Transactional(readOnly = true)
    public Map<Long, Product> getProductMap(Set<Long> productIds) {
        return productRepository.getAllById(productIds).stream()
                .collect(toMap(Product::getId, identity()));
    }

}
