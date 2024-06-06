package com.ebirdspace.urlshortenerservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;
import java.util.Map;

@Configuration
public class KafkaAdminConfig {

  private final KafkaProperties kafkaProperties;

  @Value("${kafka.urlshortener.topic-name}")
  private String topicName;

  public KafkaAdminConfig(KafkaProperties kafkaProperties) {
    this.kafkaProperties = kafkaProperties;
  }

  @Bean
  public KafkaAdmin kafkaAdmin() {
    Map<String, Object> configs = kafkaProperties.buildAdminProperties(null);
    return new KafkaAdmin(configs);
  }

  @Bean
  public NewTopic myTopic() {
    return new NewTopic(topicName, 1, (short) 1);
  }
}

