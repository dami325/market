package io.dami.market.interfaces.point;

import io.dami.market.domain.point.PointService;
import io.dami.market.interfaces.advice.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/points")
public class PointController {

  private final PointService pointService;

  @Operation(summary = "잔액 조회 API", description = "사용자의 식별자를 통해 해당 사용자의 잔액을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "잔액 조회 성공"),
      @ApiResponse(responseCode = "400", description = "파라미터 관련 에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
  })
  @GetMapping
  public ResponseEntity<PointResponse.PointDetails> getBalance(@RequestParam Long userId) {
    return ResponseEntity.ok(new PointResponse.PointDetails(pointService.getBalance(userId)));
  }

  @Operation(summary = "잔액 충전 API", description = "사용자의 식별자와 충전할 금액을 받아 해당 사용자의 잔액을 충전합니다.")
  @PostMapping
  public ResponseEntity<Void> rechargePoint(@RequestBody PointRequest.PointUpdate request) {
    pointService.chargePoint(request.userId(), request.amount());
    return ResponseEntity.ok().build();
  }

}
