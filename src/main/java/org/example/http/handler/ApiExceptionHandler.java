package org.example.http.handler;

import org.example.service.exception.SendFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {SendFailedException.class})
    public ResponseEntity<String> handleSendFailedException(SendFailedException exception) {
        String massage = "Failed to send notification on order N " + exception.getOrderNumber() +
                " by email " + exception.getCustomerEmail();
        return new ResponseEntity<>(massage, HttpStatus.BAD_GATEWAY);
    }
}
