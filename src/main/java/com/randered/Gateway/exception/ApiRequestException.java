package com.randered.Gateway.exception;

public class ApiRequestException extends RuntimeException {

    private final int errorCode;
    private final String errorMessage;

    public ApiRequestException(int errorCode, String errorMessage) {
        super(String.format("API Request failed with error code %d: %s", errorCode, errorMessage));
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
