package io.dami.market.interfaces.user;

import io.dami.market.domain.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users")
public class UserController {

  private final UserService userService;

  @Operation(summary = "사용자 쿠폰 조회", description = "사용자 식별자로 보유 쿠폰 리스트를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "쿠폰 조회 성공")
  })
  @GetMapping("/{userId}/coupons")
  public ResponseEntity<List<UserResponse.UserCouponDetails>> userCoupons(
      @PathVariable Long userId) {
    List<UserResponse.UserCouponDetails> response = userService.getUserCoupons(userId).stream()
        .map(UserResponse.UserCouponDetails::new)
        .toList();
    return ResponseEntity.ok(response);
  }
}
