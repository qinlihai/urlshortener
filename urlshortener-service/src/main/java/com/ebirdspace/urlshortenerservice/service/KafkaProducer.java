package com.ebirdspace.urlshortenerservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
  import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

  @Value("${kafka.urlshortener.topic-name}")
  private String topicName;

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendMessage(Object message) {
    kafkaTemplate.send(topicName, message);
  }
}

