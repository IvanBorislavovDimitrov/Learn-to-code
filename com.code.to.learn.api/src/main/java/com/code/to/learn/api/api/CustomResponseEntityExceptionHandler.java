package com.code.to.learn.api.api;

import com.code.to.learn.api.model.error.ErrorResponse;
import com.code.to.learn.api.parser.Parser;
import com.code.to.learn.api.parser.ParserFactory;
import com.code.to.learn.api.parser.ParserType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String DEFAULT_CONSTRAINT_VALIDATION_MESSAGE = "There is no error message for the validation";

    private final Parser parser = ParserFactory.createParser(ParserType.JSON);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .message(getErrorMessage(exception))
                .code(HttpStatus.BAD_REQUEST.value())
                .type(exception.getClass().getSimpleName())
                .build();
        return ResponseEntity.badRequest()
                .body(parser.serialize(errorResponse));
    }

    private String getErrorMessage(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        return fieldError != null ? getFormattedErrorMessage(fieldError) : DEFAULT_CONSTRAINT_VALIDATION_MESSAGE;
    }

    private String getFormattedErrorMessage(FieldError fieldError) {
        return String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage());
    }
}