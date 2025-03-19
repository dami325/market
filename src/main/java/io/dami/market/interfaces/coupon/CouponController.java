package io.dami.market.interfaces.coupon;

import io.dami.market.domain.coupon.CouponService;
import io.dami.market.interfaces.advice.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/coupons")
public class CouponController {

  private final CouponService couponService;

  @Operation(summary = "발급 가능 선착순 쿠폰 조회", description = "사용자 식별자로 발급 가능한 쿠폰을 조회합니다. 이미 발급한 쿠폰은 발급할 수 없습니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "선착순 쿠폰 조회 성공")
  })
  @GetMapping
  public ResponseEntity<List<CouponResponse.CouponDetails>> getFirstServedCoupons(
      @RequestParam Long userId) {

    List<CouponResponse.CouponDetails> response = couponService.getFirstServedCoupons(userId)
        .stream()
        .map(CouponResponse.CouponDetails::new)
        .toList();

    return ResponseEntity.ok(response);
  }

  @Operation(summary = "선착순 쿠폰 발급", description = "사용자 식별자와 쿠폰 식별자로 선착순 쿠폰을 발급합니다. 이미 발급한 쿠폰은 발급할 수 없습니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "쿠폰 발행 성공"),
      @ApiResponse(responseCode = "404", description = "쿠폰을 찾을 수 없음"),
      @ApiResponse(responseCode = "409", description = "이미 발급한 쿠폰입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PostMapping("/{couponId}")
  public ResponseEntity<Void> issueACoupon(@PathVariable Long couponId, @RequestParam Long userId) {
    couponService.issueACouponV2(couponId, userId);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

}
