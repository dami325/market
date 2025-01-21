package io.dami.market.interfaces.point;

import io.dami.market.domain.point.Point;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record PointResponse() {

    public record PointDetails(
            @Schema(example = "10000")
            BigDecimal balance
    ) {
        public PointDetails(Point point){
            this(point.getTotalPoint());
        }
    }
}
