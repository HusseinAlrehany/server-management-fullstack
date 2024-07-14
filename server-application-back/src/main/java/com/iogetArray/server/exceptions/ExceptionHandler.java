package com.iogetArray.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<Object> globalHandling(Exception ex){
        ErrorResponse response = new ErrorResponse();
        response.setMessage(ex.getMessage());
        response.setHttpStatus(HttpStatus.BAD_REQUEST);

        return ResponseEntity.badRequest().body(response);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> notFoundHandling(NotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND));
    }

}
