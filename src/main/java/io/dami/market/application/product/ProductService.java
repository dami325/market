package io.dami.market.application.product;

import io.dami.market.domain.product.Product;
import io.dami.market.domain.product.ProductRepository;
import io.dami.market.interfaces.product.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        return new PageImpl<>(content,pageable,total);
    }

    @Transactional(readOnly = true)
    public Product getProductDetails(Long productId) {
        return productRepository.getProduct(productId);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse.Top5ProductDetails> getProductsTop5() {
        return productRepository.getProductsTop5();
    }
}
