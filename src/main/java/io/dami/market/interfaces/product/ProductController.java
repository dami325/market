package io.dami.market.interfaces.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/products")
public class ProductController {

    @Operation(summary = "상품 리스트 조회 API", description = "전체 상품 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 조회 성공")
    })
    @GetMapping
    public ResponseEntity<List<ProductResponse.ProductDetails>> getProducts() {

        return ResponseEntity.ok(List.of(
                        new ProductResponse.ProductDetails(10L, "처음처럼", new BigDecimal("48000"), 30),
                        new ProductResponse.ProductDetails(9L, "C1", new BigDecimal("4900"), 30),
                        new ProductResponse.ProductDetails(8L, "대선", new BigDecimal("3500"), 30),
                        new ProductResponse.ProductDetails(7L, "새로", new BigDecimal("5600"), 15),
                        new ProductResponse.ProductDetails(6L, "진로", new BigDecimal("6000"), 30),
                        new ProductResponse.ProductDetails(5L, "카스", new BigDecimal("7000"), 2),
                        new ProductResponse.ProductDetails(4L, "테라", new BigDecimal("5400"), 3),
                        new ProductResponse.ProductDetails(3L, "참이슬", new BigDecimal("4000"), 0),
                        new ProductResponse.ProductDetails(2L, "켈리", new BigDecimal("4500"), 1),
                        new ProductResponse.ProductDetails(1L, "좋은데이", new BigDecimal("5500"), 20)
                )
        );
    }

    @Operation(summary = "상품 상세 정보 조회 API", description = "최근 3일간 가장 많이 팔린 상위 5개 상품 정보 제공")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 조회 성공"),
            @ApiResponse(responseCode = "404", description = "상품 조회 실패")
    })
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse.ProductDetails> getProductDetails(@PathVariable Long productId) {
        return ResponseEntity.ok(new ProductResponse.ProductDetails(productId, "카스", new BigDecimal("5000"), 50));
    }

    @Operation(summary = "상위 상품 조회 API", description = "최근 3일간 가장 많이 팔린 상위 5개 상품 정보 제공")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 조회 성공")
    })
    @GetMapping("/rank")
    public ResponseEntity<List<ProductResponse.Top5ProductDetails>> getProductsTop5() {

        return ResponseEntity.ok(List.of(
                        new ProductResponse.Top5ProductDetails(5L, "카스", new BigDecimal("5000"), 50,1, 500),
                        new ProductResponse.Top5ProductDetails(4L, "테라", new BigDecimal("5000"), 50,2, 400),
                        new ProductResponse.Top5ProductDetails(3L, "참이슬", new BigDecimal("4000"), 50,3, 300),
                        new ProductResponse.Top5ProductDetails(2L, "켈리", new BigDecimal("4500"), 50,4, 200),
                        new ProductResponse.Top5ProductDetails(1L, "좋은데이", new BigDecimal("5500"), 50,5, 100)
                )
        );
    }

}
