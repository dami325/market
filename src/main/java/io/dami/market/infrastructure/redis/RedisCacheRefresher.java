package io.dami.market.infrastructure.redis;

import io.dami.market.infrastructure.product.ProductQuerydslRepository;
import io.dami.market.interfaces.product.ProductResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisCacheRefresher {

  private final ProductQuerydslRepository productQuerydslRepository;
  private final CacheManager redisCacheManager;

  @Scheduled(fixedRate = 3600000)
  public void refreshTop5ProductsCache() {
    List<ProductResponse.Top5ProductDetails> updatedProducts = productQuerydslRepository.getProductsTop5();
    redisCacheManager.getCache("top5ProductDetails").put("top5", updatedProducts);
  }
}
