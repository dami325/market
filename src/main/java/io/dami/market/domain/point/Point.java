package io.dami.market.domain.point;

import io.dami.market.domain.Auditor;
import io.dami.market.domain.payment.PointNotEnoughException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Builder
@Table(name = "point")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends Auditor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "user_id", nullable = false, unique = true)
  private Long userId;

  @Comment("잔액")
  @Column(name = "total_point", nullable = false, precision = 10, scale = 2)
  private BigDecimal totalPoint;

  @Builder.Default
  @OneToMany(mappedBy = "point", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,
      CascadeType.MERGE})
  private Set<PointTransaction> pointTransactions = new HashSet<>();

  public Point(BigDecimal totalPoint) {
    this.totalPoint = totalPoint;
  }

  public void subtract(BigDecimal amount) {
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      return;
    }
    if (this.totalPoint.compareTo(amount) < 0) {
      throw new PointNotEnoughException("포인트 부족 사용 실패");
    }
    this.totalPoint = this.totalPoint.subtract(amount);
    this.pointTransactions.add(createPointTransaction(amount, PointTransaction.Type.SPEND));
  }

  public void charge(BigDecimal amount) {
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("0 이하는 충전불가");
    }
    this.totalPoint = this.totalPoint.add(amount);
    this.pointTransactions.add(createPointTransaction(amount, PointTransaction.Type.CHARGE));
  }

  private PointTransaction createPointTransaction(BigDecimal amount, PointTransaction.Type type) {
    return PointTransaction.builder()
        .type(type)
        .amount(amount)
        .point(this)
        .status(PointTransaction.Status.COMPLETED)
        .build();
  }
}
