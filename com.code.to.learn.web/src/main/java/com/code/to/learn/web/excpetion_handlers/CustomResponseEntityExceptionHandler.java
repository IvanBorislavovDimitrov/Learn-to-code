package com.code.to.learn.web.excpetion_handlers;

import com.code.to.learn.api.model.error.ApiErrorResponse;
import com.code.to.learn.persistence.util.DatabaseSessionUtil;
import com.code.to.learn.util.parser.Parser;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final Parser parser;
    private final SessionFactory sessionFactory;

    @Autowired
    public CustomResponseEntityExceptionHandler(Parser parser, SessionFactory sessionFactory) {
        this.parser = parser;
        this.sessionFactory = sessionFactory;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        DatabaseSessionUtil.closeSessionWithRollback(sessionFactory);
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse.Builder()
                .message(getErrorMessage(exception))
                .code(HttpStatus.BAD_REQUEST.value())
                .type(exception.getClass().getSimpleName())
                .build();
        return ResponseEntity.badRequest()
                .body(parser.serialize(apiErrorResponse));
    }

    private String getErrorMessage(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        return fieldError != null ? getFormattedErrorMessage(fieldError) : DEFAULT_CONSTRAINT_VALIDATION_MESSAGE;
    }

    private String getFormattedErrorMessage(FieldError fieldError) {
        return String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage());
    }
}