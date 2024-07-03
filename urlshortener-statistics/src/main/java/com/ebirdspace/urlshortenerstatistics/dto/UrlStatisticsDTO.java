package com.ebirdspace.urlshortenerstatistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlStatisticsDTO {
  private String shortCode;
  private Long clickCount;
}
