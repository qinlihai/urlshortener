package com.ebirdspace.urlshortenerstatistics.service;

import com.ebirdspace.urlshortenerstatistics.model.UrlStatistics;
import com.ebirdspace.urlshortenerstatistics.repository.UrlStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UrlStatisticsService {

  @Autowired
  private UrlStatisticsRepository repository;

  public List<UrlStatistics> getAllStatistics() {
    return repository.findAll();
  }

  public UrlStatistics getStatisticsByShortCode(String shortCode) {
    return repository.findByShortCode(shortCode);
  }
}

