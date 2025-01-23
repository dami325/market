package io.dami.market.infra.product;

import io.dami.market.domain.product.Product;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT p FROM Product p WHERE p.id IN :orderProductIds")
  List<Product> findAllByIdWithLock(Set<Long> orderProductIds);
}
