package com.code.to.learn.web.excpetion_handlers;

import com.code.to.learn.api.model.error.ApiErrorResponse;
import com.code.to.learn.persistence.exception.github.GithubException;
import com.code.to.learn.persistence.util.DatabaseSessionUtil;
import com.code.to.learn.util.parser.Parser;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GithubResponseEntityExceptionHandler {

    private final Parser parser;
    private final SessionFactory sessionFactory;

    @Autowired
    public GithubResponseEntityExceptionHandler(Parser parser, SessionFactory sessionFactory) {
        this.parser = parser;
        this.sessionFactory = sessionFactory;
    }

    @ExceptionHandler({GithubException.class})
    public ResponseEntity<String> githubErrorOccurred(Exception exception) {
        DatabaseSessionUtil.closeSessionWithRollback(sessionFactory);
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse.Builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .type(exception.getClass().getSimpleName())
                .build();
        return ResponseEntity.badRequest()
                .body(parser.serialize(apiErrorResponse));
    }
}
