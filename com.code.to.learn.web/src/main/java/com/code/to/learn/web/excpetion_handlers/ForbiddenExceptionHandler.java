package com.code.to.learn.web.excpetion_handlers;

import com.code.to.learn.api.exception.ForbiddenException;
import com.code.to.learn.api.model.error.ApiErrorResponse;
import com.code.to.learn.util.parser.Parser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ForbiddenExceptionHandler {

    private final Parser parser;

    public ForbiddenExceptionHandler(Parser parser) {
        this.parser = parser;
    }

    @ExceptionHandler({ForbiddenException.class})
    public ResponseEntity<String> clientError(Exception exception) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse.Builder()
                .code(HttpStatus.FORBIDDEN.value())
                .message(exception.getMessage())
                .type(exception.getClass().getSimpleName())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value())
                .body(parser.serialize(apiErrorResponse));
    }

}
