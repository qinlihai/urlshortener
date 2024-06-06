package com.ebirdspace.urlshortenerstatistics.service;

import com.ebirdspace.urlshortenerstatistics.model.UrlStatistics;
import com.ebirdspace.urlshortenerstatistics.repository.UrlStatisticsRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

  private final UrlStatisticsRepository urlStatisticsRepository;

  public KafkaConsumer(
      UrlStatisticsRepository urlStatisticsRepository) {
    this.urlStatisticsRepository = urlStatisticsRepository;
  }

  @KafkaListener(topics = "${kafka.urlshortener.topic-name}",
      groupId = "${spring.kafka.consumer.group-id}")
  public void consume(String message) {

    if(message != null && message.startsWith("Redirect url:")) {
      String shortCode = message.substring("Redirect url: ".length(), message.indexOf(" to "));
      UrlStatistics statistics = urlStatisticsRepository.findByShortCode(shortCode);
      if(statistics != null) {
        statistics.setClickCount(statistics.getClickCount() + 1);
      }else {
        statistics = new UrlStatistics();
        statistics.setShortCode(shortCode);
        statistics.setClickCount(1L);
      }
      urlStatisticsRepository.save(statistics);
    }

    System.out.println("Consumed message: " + message);
  }
}
