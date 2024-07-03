package com.ebirdspace.urlshortenerstatistics.repository;


import com.ebirdspace.urlshortenerstatistics.model.UrlStatistics;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlStatisticsRepository extends JpaRepository<UrlStatistics, Long> {
  Optional<UrlStatistics> findByShortCode(String shortCode);
}

