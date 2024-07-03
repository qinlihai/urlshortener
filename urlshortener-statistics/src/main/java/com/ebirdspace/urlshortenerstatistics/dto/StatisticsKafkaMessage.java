package com.ebirdspace.urlshortenerstatistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsKafkaMessage {
  private String originalUrl;
  private String shortUrl;
  private String shortCode;
}
