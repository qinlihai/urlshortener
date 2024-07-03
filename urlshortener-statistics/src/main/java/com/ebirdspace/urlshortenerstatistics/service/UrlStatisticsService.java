package com.ebirdspace.urlshortenerstatistics.service;

import com.ebirdspace.urlshortenerstatistics.dto.UrlStatisticsDTO;
import com.ebirdspace.urlshortenerstatistics.mapper.UrlStatisticsMapper;
import com.ebirdspace.urlshortenerstatistics.model.UrlStatistics;
import com.ebirdspace.urlshortenerstatistics.repository.UrlStatisticsRepository;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UrlStatisticsService {

  private final UrlStatisticsRepository repository;
  private final UrlStatisticsMapper urlStatisticsMapper;

  public List<UrlStatisticsDTO> getAllStatistics() {
    return repository.findAll().stream().
        map(urlStatisticsMapper::urlStatisticsToUrlStatisticsDTO).collect(
            Collectors.toList());
  }

  public UrlStatisticsDTO getStatisticsByShortCode(String shortCode) {
    Optional<UrlStatistics> urlStatistics = repository.findByShortCode(shortCode);
    if(urlStatistics.isPresent()) {
      return urlStatisticsMapper.urlStatisticsToUrlStatisticsDTO(urlStatistics.get());
    } else {
      return null;
    }
  }
}

