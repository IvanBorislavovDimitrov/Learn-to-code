package com.code.to.learn.api.model.error;

public class ApiErrorResponse {

    private int code;
    private String type;
    private String message;

    private ApiErrorResponse() {

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
        private ApiErrorResponse apiErrorResponse = new ApiErrorResponse();

        public Builder code(int status) {
            apiErrorResponse.code = status;
            return this;
        }

        public Builder type(String type) {
            apiErrorResponse.type = type;
            return this;
        }

        public Builder message(String message) {
            apiErrorResponse.message = message;
            return this;
        }

        public ApiErrorResponse build() {
            return apiErrorResponse;
        }

    }
}
