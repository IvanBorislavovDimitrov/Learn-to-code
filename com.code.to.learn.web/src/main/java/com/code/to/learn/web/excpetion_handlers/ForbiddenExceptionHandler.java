package com.code.to.learn.web.excpetion_handlers;

import com.code.to.learn.api.exception.ForbiddenException;
import com.code.to.learn.api.model.error.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@ControllerAdvice
public class ForbiddenExceptionHandler {

    @ExceptionHandler({ForbiddenException.class})
    public ResponseEntity<ApiErrorResponse> clientError(Exception exception) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse.Builder()
                .code(HttpStatus.FORBIDDEN.value())
                .message(exception.getMessage())
                .type(exception.getClass().getSimpleName())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value())
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(apiErrorResponse);
    }

}
