package io.dami.market.interfaces.payment;

import io.dami.market.application.payment.PaymentFacade;
import io.dami.market.interfaces.advice.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/payment")
public class PaymentController {

  private final PaymentFacade paymentFacade;

  @Operation(summary = "결제하기")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "결제 성공"),
      @ApiResponse(responseCode = "400", description = "파라미터 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "유효하지 않은 식별자", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "480", description = "포인트 부족 주문 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "481", description = "재고 부족", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "482", description = "이미 결제된 주문", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "483", description = "이미 사용된 쿠폰", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "484", description = "취소된 주문", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
  })
  @PostMapping
  public ResponseEntity<Void> pay(@RequestBody PaymentRequest.Pay request) {
    paymentFacade.processOrderPayment(request.userId(), request.orderId());
    return ResponseEntity.ok(null);
  }
}
