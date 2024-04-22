package com.lesso.neverland.common.exception;

import com.lesso.neverland.common.base.BaseException;
import com.lesso.neverland.common.base.ErrorResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.lesso.neverland.common.base.BaseResponseStatus.DUPLICATE_RESOURCE;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { ConstraintViolationException.class, DataIntegrityViolationException.class})
    protected ResponseEntity<ErrorResponse> handleDataException() {
        return ErrorResponse.toResponseEntity(DUPLICATE_RESOURCE);
    }

    @ExceptionHandler(value = { BaseException.class })
    protected ResponseEntity<ErrorResponse> handleCustomException(BaseException e) {
        return ErrorResponse.toResponseEntity(e.getStatus());
    }
}
