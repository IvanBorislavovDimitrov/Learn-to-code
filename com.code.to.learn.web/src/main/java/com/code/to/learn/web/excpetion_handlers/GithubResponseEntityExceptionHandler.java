package com.code.to.learn.web.excpetion_handlers;

import com.code.to.learn.api.model.error.ApiErrorResponse;
import com.code.to.learn.persistence.exception.github.GithubException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GithubResponseEntityExceptionHandler {

    @ExceptionHandler({GithubException.class})
    public ResponseEntity<ApiErrorResponse> githubErrorOccurred(Exception exception, WebRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse.Builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .type(exception.getClass().getSimpleName())
                .build();
        return ResponseEntity.badRequest()
                .body(apiErrorResponse);
    }
}
