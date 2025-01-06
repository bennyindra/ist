package com.ist.main.exception;

import io.swagger.v3.oas.annotations.Hidden;
import java.util.List;
import java.util.Map;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Hidden
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Object> handleApplicationException(ApplicationException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "errors",
                        List.of(e.getMessage()),
                        "message",
                        "ApplicationException",
                        "status",
                        String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "errors",
                        errors,
                        "message",
                        "Validation Exception",
                        "status",
                        String.valueOf(HttpStatus.BAD_REQUEST.value())));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleApplicationException(BusinessException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(Map.of(
                        "errors",
                        List.of(e.getErrorMessage()),
                        "message",
                        "BusinessException",
                        "status",
                        String.valueOf(e.getHttpStatus().value())));
    }
}
