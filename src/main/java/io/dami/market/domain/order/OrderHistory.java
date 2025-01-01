package io.dami.market.domain.order;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "tb_order_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("주문 이력 고유 ID")
    @Column(name = "id")
    private Long id;

    @Comment("주문 ID (외래 키)")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Comment("주문 상태 (예: 대기, 완료, 실패)")
    @Column(name = "status", nullable = false)
    private String status;

    @Comment("상태 변경 일시")
    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;
}
