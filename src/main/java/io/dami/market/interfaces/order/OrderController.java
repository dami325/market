package io.dami.market.interfaces.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/orders")
public class OrderController {

    @Operation(summary = "주문 하기", description = """
            사용자 식별자와 상품 수량 목록을 입력받아 주문하고 결제를 수행합니다.
            잔액이 부족할 경우 결제가 실패하고 주문상태는 결제 대기 상태로 변경됩니다. 
            주문 시에 유효한 할인 쿠폰을 함께 제출하면, 전체 주문금액에 대해 할인 혜택을 부여받을 수 있습니다.
            쿠폰은 주문 시 1개만 사용 가능합니다.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 성공, 결제 성공"),
            @ApiResponse(responseCode = "202", description = "주문 성공, 결제 실패"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류"),
            @ApiResponse(responseCode = "422", description = "유효하지 않은 할인 쿠폰"),
    })
    @PostMapping
    public ResponseEntity<Void> doOrder(@RequestBody OrderRequest.OrderCreate request) {

        return ResponseEntity.ok(null);
    }
}
