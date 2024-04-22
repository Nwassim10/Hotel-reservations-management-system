package edu.miu.cs.cs544.config;

import edu.miu.cs.cs544.domain.CustomError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {
    //catches all the exception thrown by ProductService class and returns a response entity
    @ExceptionHandler(CustomError.class)
    public ResponseEntity<?> handleRestErrors(CustomError err) {
        Map<String, Object> map = new HashMap<>();
        map.put("error", err.getMessage());
        return new ResponseEntity<>(map, err.getStatusCode() == null ? HttpStatus.BAD_REQUEST : err.getStatusCode());
    }
}
