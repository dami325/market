package io.dami.market.domain.point;

import io.dami.market.domain.Auditor;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "point_transaction")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointTransaction extends Auditor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_point_id", nullable = false)
    private Point point;

    @Comment("거래 금액")
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Comment("거래 유형")
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private Type type;

    @Comment("거래 설명")
    @Column(name = "description")
    private String description;

    @Comment("거래 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @RequiredArgsConstructor
    public enum Status {
        PENDING("대기"),
        COMPLETED("완료"),
        FAILED("실패"),
        ;
        private final String description;
    }

    @RequiredArgsConstructor
    public enum Type {
        CHARGE("포인트 충전"),
        SPEND("포인트 사용"),
        REFUND("환불 처리"),
        EXPIRATION("포인트 만료 처리");

        private final String description;
    }
}
