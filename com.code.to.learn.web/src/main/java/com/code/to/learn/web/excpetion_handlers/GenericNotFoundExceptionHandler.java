package com.code.to.learn.web.excpetion_handlers;

import com.code.to.learn.api.model.error.ApiErrorResponse;
import com.code.to.learn.persistence.exception.basic.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@ControllerAdvice
public class GenericNotFoundExceptionHandler {

    @ExceptionHandler({NotFoundException.class, UsernameNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> userExceptionOccurred(Exception exception) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse.Builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .type(exception.getClass().getSimpleName())
                .build();
        return ResponseEntity.badRequest()
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(apiErrorResponse);
    }
}
