package com.code.to.learn.web.excpetion_handlers;

import com.code.to.learn.api.model.error.ApiErrorResponse;
import com.code.to.learn.persistence.exception.basic.NotFoundException;
import com.code.to.learn.util.parser.Parser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GenericNotFoundExceptionHandler {

    private final Parser parser;

    public GenericNotFoundExceptionHandler(Parser parser) {
        this.parser = parser;
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<String> userExceptionOccurred(Exception exception) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse.Builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .type(exception.getClass().getSimpleName())
                .build();
        return ResponseEntity.badRequest()
                .body(parser.serialize(apiErrorResponse));
    }
}
