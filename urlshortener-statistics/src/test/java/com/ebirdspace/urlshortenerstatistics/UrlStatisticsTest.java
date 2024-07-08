package com.ebirdspace.urlshortenerstatistics;

import com.ebirdspace.urlshortenerstatistics.dto.StatisticsKafkaMessage;
import com.ebirdspace.urlshortenerstatistics.model.UrlStatistics;
import com.ebirdspace.urlshortenerstatistics.repository.UrlStatisticsRepository;
import com.ebirdspace.urlshortenerstatistics.service.UrlStatisticsService;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.awaitility.Duration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.*;

//@Disabled
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Testcontainers
@DirtiesContext
@EmbeddedKafka(topics = {"${kafka.urlshortener.topic-name}"})
public class UrlStatisticsTest {

  static Logger log = LoggerFactory.getLogger(UrlStatisticsTest.class);

  @Autowired
  private UrlStatisticsService urlStatisticsService;

  @Autowired
  private UrlStatisticsRepository urlStatisticsRepository;

  @Value("${kafka.urlshortener.topic-name}")
  private String kafkaTopic;

  @Autowired
  private KafkaTemplate<String, Object> kafkaTemplate;

  @Autowired
  private ConsumerFactory<String, StatisticsKafkaMessage> consumerFactory;

  @Container
  public static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.26")
      .withDatabaseName("test_db")
      .withUsername("test")
      .withPassword("test");

  @BeforeAll
  static void setup() {
    log.info("@BeforeAll - executes once before all test methods in this class");
  }

  @BeforeEach
  void init() {
    log.info("@BeforeEach - executes before each test method in this class");
  }

  @AfterEach
  void tearDown() {
    log.info("@AfterEach - executed after each test method.");
  }

  @AfterAll
  static void done() {
    log.info("@AfterAll - executed after all test methods.");
  }

  @Test
  public void testUrlStatisticsService() {
    String shortCode = "4t3ZEp";
    UrlStatistics urlStatistics = new UrlStatistics();
    urlStatistics.setShortCode(shortCode);
    urlStatistics.setClickCount(1L);
    urlStatisticsRepository.save(urlStatistics);
    List<UrlStatistics> urlStatisticsList = urlStatisticsRepository.findAll();

    assertNotNull(urlStatisticsService.getStatisticsByShortCode(shortCode));
    assertTrue(urlStatisticsService.getAllStatistics().size() == urlStatisticsList.size());
  }


  @Test
  public void testKafkaConsumer() {
    String shortCode = "4t3ZEi";
    String originalUrl = "www.yahoo.com";
    StatisticsKafkaMessage kafkaMessage = new StatisticsKafkaMessage(originalUrl, "http://localhost:8080/" + shortCode, shortCode);
    CompletableFuture<SendResult<String, Object>> sendResult =  kafkaTemplate.send(kafkaTopic, kafkaMessage);
//
//    sendResult.whenComplete((result, ex) -> {
//      assertTrue(ex == null);
//    });

//    Verify the kafka consumer listener KafkaConsumer
    await()
        .pollInterval(Duration.TWO_SECONDS)
        .atMost(Duration.TEN_SECONDS)
        .untilAsserted(() -> {
          Optional<UrlStatistics> urlStatisticsOptional = urlStatisticsRepository.findByShortCode(shortCode);
          assertTrue(urlStatisticsOptional.isPresent());
          assertTrue(urlStatisticsOptional.get().getShortCode().equals(shortCode));
        });
  }

}

