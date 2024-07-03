package com.ebirdspace.urlshortenerservice.config;

import com.ebirdspace.urlshortenerservice.dto.StatisticsKafkaMessage;
import java.util.Map;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@EnableKafka
@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaConfig {

  private final KafkaProperties kafkaProperties;

  public KafkaConfig(KafkaProperties kafkaProperties) {
    this.kafkaProperties = kafkaProperties;
  }

  @Bean
  public ProducerFactory<String, Object> producerFactory() {
    Map<String, Object> configProps = kafkaProperties.buildProducerProperties(null);
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public KafkaTemplate<String, Object> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  @Bean
  public ConsumerFactory<String, StatisticsKafkaMessage> consumerFactory() {
    Map<String, Object> configProps = kafkaProperties.buildConsumerProperties(null);
    return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(),
        new ErrorHandlingDeserializer<>(new JsonDeserializer<>(StatisticsKafkaMessage.class)));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, StatisticsKafkaMessage> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, StatisticsKafkaMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
  }
}
