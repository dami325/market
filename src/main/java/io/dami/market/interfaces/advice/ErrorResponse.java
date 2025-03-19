package io.dami.market.interfaces.advice;

import io.swagger.v3.oas.annotations.media.Schema;

public record ErrorResponse(
    @Schema(example = "XXX_EXCEPTION")
    String errorCode,
    @Schema(example = "유효하지 않습니다.")
    String message
) {

}
