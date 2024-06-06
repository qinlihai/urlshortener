package com.ebirdspace.urlshortenerstatistics.controller;


import com.ebirdspace.urlshortenerstatistics.model.UrlStatistics;
import com.ebirdspace.urlshortenerstatistics.service.UrlStatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins="*", allowedHeaders = "*", methods = {RequestMethod.GET})
@RestController
@RequestMapping("/api/v1/urlstatistics")
public class UrlStatisticsController {

  private final UrlStatisticsService statisticsService;

  public UrlStatisticsController(UrlStatisticsService statisticsService) {
    this.statisticsService = statisticsService;
  }

  @GetMapping("/")
  public ResponseEntity<List<UrlStatistics>> getAllStatistics() {
    return ResponseEntity.ok(statisticsService.getAllStatistics());
  }

  @GetMapping("/{shortCode}")
  public ResponseEntity<UrlStatistics> getStatisticsByShortCode(@PathVariable String shortCode) {
    UrlStatistics statistics = statisticsService.getStatisticsByShortCode(shortCode);
    if (statistics != null) {
      return ResponseEntity.ok(statistics);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}

