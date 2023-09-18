package com.alukianov.FileServer.exceptions;

import com.alukianov.FileServer.models.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Response> handleMaxSizeException(MaxUploadSizeExceededException exception) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(Response.builder()
                .status(HttpStatus.EXPECTATION_FAILED.value())
                .message(exception.getMessage())
                .payload(null)
                .build());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Response> noSuchElementException(NoSuchElementException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .payload(null)
                .build());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response> runtimeException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(exception.getMessage())
                .payload(null)
                .build());
    }
    
}
