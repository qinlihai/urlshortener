package com.ebirdspace.urlshortenerservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

  // Handle global exceptions
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorInfo> handleGlobalException(Exception ex, WebRequest request) {
    ErrorInfo errorDetails = new ErrorInfo(new Date(), ex.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
