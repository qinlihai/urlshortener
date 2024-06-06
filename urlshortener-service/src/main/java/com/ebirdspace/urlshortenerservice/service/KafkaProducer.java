package com.ebirdspace.urlshortenerservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
  import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

  @Value("${kafka.urlshortener.topic-name}")
  private String topicName;

  private final KafkaTemplate<String, String> kafkaTemplate;

  public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendMessage(String message) {
    kafkaTemplate.send(topicName, message);
  }
}

