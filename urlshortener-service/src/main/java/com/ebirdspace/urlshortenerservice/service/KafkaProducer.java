package com.ebirdspace.urlshortenerservice.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
  import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
  private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

  @Value("${kafka.urlshortener.topic-name}")
  private String topicName;

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @CircuitBreaker(name = "kafkaProducer")
  public void sendMessage(Object message) {
    kafkaTemplate.send(topicName, message);
  }

//  public void logMessageToDeadLetterQueue(Throwable throwable) {
//    logger.info("Fallback method is called.");
//  }
}

