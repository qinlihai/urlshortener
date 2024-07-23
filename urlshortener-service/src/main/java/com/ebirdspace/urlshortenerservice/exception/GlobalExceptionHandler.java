package com.ebirdspace.urlshortenerservice.exception;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({CallNotPermittedException.class})
  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  public void handleCallNotPermittedException() {
  }

  @ExceptionHandler({BadCredentialsException.class})
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public void handleBadCredentialsException() {
  }

  @ExceptionHandler({AccountStatusException.class, AccessDeniedException.class, SignatureException.class, ExpiredJwtException.class})
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public void handleOtherAuthenticationException() {
  }

  // Handle global exceptions
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorInfo> handleGlobalException(Exception ex, WebRequest request) {
    ErrorInfo errorDetails = new ErrorInfo(new Date(), ex.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
