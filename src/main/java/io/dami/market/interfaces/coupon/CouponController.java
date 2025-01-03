package io.dami.market.interfaces.coupon;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/coupons")
public class CouponController {

    @Operation(summary = "발급 가능 선착순 쿠폰 조회",description = "사용자 식별자로 발급 가능한 쿠폰을 조회합니다. 이미 발급한 쿠폰은 발급할 수 없습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "선착순 쿠폰 조회 성공")
    })
    @GetMapping
    public ResponseEntity<List<CouponResponse.CouponDetails>> getCouponDetails(@RequestParam Long userId) {

        return ResponseEntity.ok(
                List.of(
                        new CouponResponse.CouponDetails(1L, "새해 맞이 쿠폰", new BigDecimal("5000"), 10, 0, LocalDateTime.now(), LocalDateTime.now().plusDays(3)),
                        new CouponResponse.CouponDetails(2L, "이벤트 쿠폰", new BigDecimal("3000"), 20, 0, LocalDateTime.now(), LocalDateTime.now().plusDays(5))
                )
        );
    }

    @Operation(summary = "선착순 쿠폰 발급",description = "사용자 식별자와 쿠폰 식별자로 선착순 쿠폰을 발급합니다. 이미 발급한 쿠폰은 발급할 수 없습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "쿠폰 발행 성공"),
            @ApiResponse(responseCode = "409", description = "이미 발급한 쿠폰입니다.")
    })
    @PostMapping("/{couponId}")
    public ResponseEntity<Void> couponIssued(@PathVariable Long couponId, @RequestParam Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
