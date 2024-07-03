package com.ebirdspace.urlshortenerstatistics.service;

import com.ebirdspace.urlshortenerstatistics.dto.StatisticsKafkaMessage;
import com.ebirdspace.urlshortenerstatistics.model.UrlStatistics;
import com.ebirdspace.urlshortenerstatistics.repository.UrlStatisticsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class KafkaConsumer {

  private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

  private final UrlStatisticsRepository urlStatisticsRepository;

  public KafkaConsumer(
      UrlStatisticsRepository urlStatisticsRepository) {
    this.urlStatisticsRepository = urlStatisticsRepository;
  }

  @KafkaListener(topics = "${kafka.urlshortener.topic-name}",
      groupId = "${spring.kafka.consumer.group-id}")
  public void consume(StatisticsKafkaMessage message) {

    if(message != null) {
      String shortCode = message.getShortCode();
      Optional<UrlStatistics> statisticsOptional = urlStatisticsRepository.findByShortCode(shortCode);
      UrlStatistics statistics;
      if(statisticsOptional.isPresent()) {
        statistics = statisticsOptional.get();
        statistics.setClickCount(statistics.getClickCount() + 1);
      }else {
        statistics = new UrlStatistics();
        statistics.setShortCode(shortCode);
        statistics.setClickCount(1L);
      }
      urlStatisticsRepository.save(statistics);
    }

    logger.debug("Consumed message: " + message);
  }
}
