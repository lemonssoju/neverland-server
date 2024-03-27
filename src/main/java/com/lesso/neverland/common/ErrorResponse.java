package com.lesso.neverland.common;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(BaseResponseStatus status) {
        return ResponseEntity
                .status(status.getHttpStatus())
                .body(ErrorResponse.builder()
                        .status(status.getHttpStatus().value())
                        .error(status.getHttpStatus().name())
                        .code(status.name())
                        .message(status.getMessage())
                        .build());
    }
}
