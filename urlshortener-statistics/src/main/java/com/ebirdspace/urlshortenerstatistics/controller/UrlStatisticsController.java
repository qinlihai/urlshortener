package com.ebirdspace.urlshortenerstatistics.controller;


import com.ebirdspace.urlshortenerstatistics.dto.UrlStatisticsDTO;
import com.ebirdspace.urlshortenerstatistics.dto.UrlStatisticsRequest;
import com.ebirdspace.urlshortenerstatistics.service.UrlStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/urlstatistics")
public class UrlStatisticsController {

  private final UrlStatisticsService statisticsService;

  @GetMapping
  public ResponseEntity<List<UrlStatisticsDTO>> getAllStatistics() {
    return ResponseEntity.ok(statisticsService.getAllStatistics());
  }

  @PostMapping
  public ResponseEntity<UrlStatisticsDTO> getStatisticsByShortCode(@RequestBody UrlStatisticsRequest statisticsRequest) {
    UrlStatisticsDTO statistics = statisticsService.getStatisticsByShortCode(statisticsRequest.getShortCode());
    if (statistics != null) {
      return ResponseEntity.ok(statistics);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}

