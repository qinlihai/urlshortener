package com.ebirdspace.urlshortenerservice;

import com.ebirdspace.urlshortenerservice.dto.UrlDTO;
import com.ebirdspace.urlshortenerservice.service.KafkaProducer;
import com.ebirdspace.urlshortenerservice.service.UrlService;
import com.ebirdspace.urlshortenerservice.util.Constants;
import com.ebirdspace.urlshortenerservice.util.UrlUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.Assert.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Testcontainers
public class UrlTest {

  static Logger log = LoggerFactory.getLogger(UrlTest.class);

  @Autowired
  private UrlService urlService;

  @Autowired
  private UrlUtil urlUtil;

  @MockBean
  KafkaProducer kafkaProducer;

  @MockBean
  KafkaAdmin kafkaAdmin;

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
    Mockito.doNothing().when(kafkaProducer).sendMessage(Mockito.anyString());
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
  public void testUrlService() {
    UrlDTO urlDTO = urlService.shortenUrl("https://www.yahoo.com");
    UrlDTO urlDTO1 = urlService.shortenUrl("https://www.yahoo.com");

    assertEquals(urlDTO.getShortUrl(), urlDTO1.getShortUrl());
  }

  @Test
  public void testUrlShortener() {
    String shortCode = urlUtil.generateShortCode();
    assertTrue(shortCode.length() == Constants.SHORTCODE_LENGTH);
  }

  @Test
  public void testUrlValidator() {
    assertTrue(urlUtil.isValidUrl("www.yahoo.com"));
    assertTrue(urlUtil.isValidUrl("file://www.yahoo.com"));
    assertTrue(urlUtil.isValidUrl("http://www.yahoo.com"));
    assertFalse(urlUtil.isValidUrl("ttt://www.yahoo.com"));

  }
}

