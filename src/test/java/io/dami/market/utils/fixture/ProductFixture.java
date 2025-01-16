package io.dami.market.utils.fixture;

import io.dami.market.domain.product.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record ProductFixture() {

    public static Product product(String name){
        return Product.builder()
                .name(name)
                .price(BigDecimal.valueOf(5000))
                .stockQuantity(100)
                .build();
    }

    public static Product product(String name, int price){
        return Product.builder()
                .name(name)
                .price(BigDecimal.valueOf(price))
                .stockQuantity(100)
                .build();
    }

    public static Product product(Long id,String name, int price){
        return Product.builder()
                .id(id)
                .name(name)
                .price(BigDecimal.valueOf(price))
                .stockQuantity(100)
                .build();
    }

    public static Product product(String name, int price, int stockQuantity){
        return Product.builder()
                .name(name)
                .price(BigDecimal.valueOf(price))
                .stockQuantity(stockQuantity)
                .build();
    }

    public static List<Product> products(Map<String,Integer> productMap) {
        ArrayList<Product> products = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : productMap.entrySet()) {
            products.add(product(entry.getKey(),entry.getValue()));
        }
        return products;
    }
}
