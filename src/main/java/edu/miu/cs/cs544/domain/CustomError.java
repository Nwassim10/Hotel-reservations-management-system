package edu.miu.cs.cs544.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

public class CustomError extends Exception {
    private final String message;
    @Getter
    @Setter
    private HttpStatus statusCode = HttpStatus.BAD_REQUEST;

    public CustomError(String message) {
        super(message);
        this.message = message;
    }

    public CustomError(String message, HttpStatus statusCode) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }

}
