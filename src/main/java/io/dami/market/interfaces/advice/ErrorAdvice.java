package io.dami.market.interfaces.advice;

import io.dami.market.domain.payment.PointNotEnoughException;
import io.dami.market.domain.product.ProductIsOutOfStock;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ErrorAdvice {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> illegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("BAD_REQUEST", e.getMessage()));
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ErrorResponse> entityNotFoundException(EntityNotFoundException e) {
        log.warn(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("NOT_FOUND", e.getMessage()));
    }

    @ExceptionHandler({PointNotEnoughException.class})
    public ResponseEntity<ErrorResponse> pointNotEnoughException(PointNotEnoughException e) {
        log.warn(e.getMessage(), e);
        return ResponseEntity
                .status(480)
                .body(new ErrorResponse("POINT_NOT_ENOUGH", e.getMessage()));
    }
    @ExceptionHandler({ProductIsOutOfStock.class})
    public ResponseEntity<ErrorResponse> productIsOutOfStock(ProductIsOutOfStock e) {
        log.warn(e.getMessage(), e);
        return ResponseEntity
                .status(481)
                .body(new ErrorResponse("PRODUCT_IS_OUT_OF_STOCK", e.getMessage()));
    }
}
