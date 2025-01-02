package io.dami.market.interfaces.user;

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
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users")
public class UserController {

    @Operation(summary = "보유 쿠폰 조회", description = "사용자 식별자로 보유 쿠폰 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "쿠폰 조회 성공")
    })
    @GetMapping("/{userId}/coupons")
    public ResponseEntity<List<UserResponse.UserCoupon>> userCoupons(@PathVariable Long userId) {

        return ResponseEntity.ok(
                List.of(
                        new UserResponse.UserCoupon(1L, "새해 맞이 쿠폰", new BigDecimal("5000"), LocalDateTime.now(), LocalDateTime.now().plusDays(3), LocalDateTime.now()),
                        new UserResponse.UserCoupon(2L, "이벤트 쿠폰", new BigDecimal("3000"), LocalDateTime.now(), LocalDateTime.now().plusDays(5), LocalDateTime.now())
                )
        );
    }
}
