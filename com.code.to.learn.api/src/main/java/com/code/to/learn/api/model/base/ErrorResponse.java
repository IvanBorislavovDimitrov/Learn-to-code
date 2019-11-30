package com.code.to.learn.api.model.base;

public class ErrorResponse {

    private int code;
    private String type;
    private String message;

    private ErrorResponse() {

    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public static class Builder {
        private ErrorResponse errorResponse = new ErrorResponse();

        public Builder code(int status) {
            errorResponse.code = status;
            return this;
        }

        public Builder type(String type) {
            errorResponse.type = type;
            return this;
        }

        public Builder message(String message) {
            errorResponse.message = message;
            return this;
        }

        public ErrorResponse build() {
            return errorResponse;
        }

    }
}
