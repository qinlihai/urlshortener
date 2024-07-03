package com.ebirdspace.urlshortenerstatistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlStatisticsRequest {
  private String shortCode;
}
