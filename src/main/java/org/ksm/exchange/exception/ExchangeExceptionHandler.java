package org.ksm.exchange.exception;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@ControllerAdvice
public class ExchangeExceptionHandler extends ResponseEntityExceptionHandler {

    ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleExceptions(Exception e, WebRequest request) throws IOException {
        String error = String.format("Message [%s]", e.getMessage());
        String classname = String.format("%s", e.getClass().getName());

        Map<String, String> data = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        data.put("error", error);
        data.put("className", classname);
        String errorMessage = objectMapper.writeValueAsString(data);

        return new ResponseEntity<Object>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders headers, HttpStatus httpStatus,
                                                               WebRequest request) {
        return new ResponseEntity<Object>(ex.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        return new ResponseEntity<Object>(ex.toString(), HttpStatus.BAD_REQUEST);
    }
}
