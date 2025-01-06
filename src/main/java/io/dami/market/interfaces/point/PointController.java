package io.dami.market.interfaces.point;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/point")
public class PointController {

    @Operation(summary = "잔액 조회 API", description = "사용자의 식별자를 통해 해당 사용자의 잔액을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "잔액 조회 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 관련 에러"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
    })
    @GetMapping
    public ResponseEntity<PointResponse.WalletDetails> getBalance(@RequestParam Long userId) {

        return ResponseEntity.ok(new PointResponse.WalletDetails(userId, new BigDecimal("1000")));
    }

    @Operation(summary = "잔액 충전 API", description = "사용자의 식별자와 충전할 금액을 받아 해당 사용자의 잔액을 충전합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "잔액 충전 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 관련 에러"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
    })
    @PostMapping
    public ResponseEntity<PointResponse.WalletDetails> chargeWallet(@RequestBody PointRequest.ChargeWallet request) {

        return ResponseEntity.ok(null);
    }

}
