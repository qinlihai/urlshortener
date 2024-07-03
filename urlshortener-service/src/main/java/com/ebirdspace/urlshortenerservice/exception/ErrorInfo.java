package com.ebirdspace.urlshortenerservice.exception;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorInfo {
  private Date timestamp;
  private String message;
  private String details;
}

