package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.service.impl.GeneralException;
import org.everit.json.schema.ValidationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * Controller that handles exceptions
 */

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionMessage extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<String> handleGeneralException(GeneralException e) {
        return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }




}
