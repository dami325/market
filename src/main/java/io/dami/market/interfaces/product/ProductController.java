package io.dami.market.interfaces.product;

import io.dami.market.domain.product.ProductService;
import io.dami.market.interfaces.advice.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/products")
public class ProductController {

  private final ProductService productService;

  @Operation(summary = "상품 리스트 조회 API", description = "전체 상품 리스트를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "상품 조회 성공")
  })
  @GetMapping
  public ResponseEntity<Page<ProductResponse.ProductDetails>> getProducts(
      @RequestParam(defaultValue = "0") int page) {
    return ResponseEntity.ok(productService.getProducts(PageRequest.of(page, 10)));
  }

  @Operation(summary = "상품 상세 정보 조회 API")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "상품 조회 성공"),
      @ApiResponse(responseCode = "404", description = "상품 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping("/{productId}")
  public ResponseEntity<ProductResponse.ProductDetails> getProductDetails(
      @PathVariable Long productId) {
    return ResponseEntity.ok(
        new ProductResponse.ProductDetails(productService.getProductDetails(productId)));
  }

  @Operation(summary = "상위 상품 조회 API", description = "최근 3일간 가장 많이 팔린 상위 5개 상품 정보 제공")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "상품 조회 성공")
  })
  @GetMapping("/rank")
  public ResponseEntity<List<ProductResponse.Top5ProductDetails>> getProductsTop5() {
    return ResponseEntity.ok(productService.getProductsTop5());
  }

}
