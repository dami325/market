package io.dami.market.domain.product;

import io.dami.market.domain.Auditor;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends Auditor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("상품 고유 ID")
    @Column(name = "id")
    private Long id;

    @Comment("상품 이름")
    @Column(name = "name", nullable = false)
    private String name;

    @Comment("상품 가격")
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Comment("상품 재고 수량")
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

}
