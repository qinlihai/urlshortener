package com.ebirdspace.urlshortenerstatistics.repository;


import com.ebirdspace.urlshortenerstatistics.model.UrlStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlStatisticsRepository extends JpaRepository<UrlStatistics, Long> {
  UrlStatistics findByShortCode(String shortCode);
}

