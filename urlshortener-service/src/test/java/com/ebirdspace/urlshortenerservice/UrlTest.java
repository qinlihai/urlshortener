package com.ebirdspace.urlshortenerservice;

import com.ebirdspace.urlshortenerservice.dto.UrlDTO;
import com.ebirdspace.urlshortenerservice.model.Url;
import com.ebirdspace.urlshortenerservice.repository.UrlRepository;
import com.ebirdspace.urlshortenerservice.service.KafkaProducer;
import com.ebirdspace.urlshortenerservice.service.UrlService;
import com.ebirdspace.urlshortenerservice.util.Constants;
import com.ebirdspace.urlshortenerservice.util.UrlUtil;
import java.util.Optional;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.Assert.*;

//@Disabled
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Testcontainers
@EmbeddedKafka
@DirtiesContext
public class UrlTest {

  static Logger log = LoggerFactory.getLogger(UrlTest.class);

  @Autowired
  private UrlService urlService;

  @Autowired
  private UrlRepository urlRepository;

  @Autowired
  private UrlUtil urlUtil;

  @MockBean
  KafkaProducer kafkaProducer;

  @MockBean
  KafkaAdmin kafkaAdmin;

  @Value("urlshortener.shorturl.host")
  private String shortUrlHost;

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
  public void testUrlService() {
    String originalUrl = "https://www.yahoo.com";
    UrlDTO urlDTO = urlService.shortenUrl(originalUrl);
    UrlDTO urlDTO1 = urlService.shortenUrl(originalUrl);
    Optional<Url> urlOptional = urlRepository.findByOriginalUrl(originalUrl);
    assertTrue(urlOptional.isPresent());
    String shortCode = urlOptional.get().getShortCode();
    assertEquals(urlDTO.getShortUrl(), urlDTO1.getShortUrl());
    assertTrue(urlDTO.getOriginalUrl().equals(originalUrl));
    assertEquals(urlDTO.getShortUrl(), shortUrlHost + "/" + shortCode);
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
    String originalUrl = "https://www.yahoo.com/";
    assertFalse(urlUtil.isValidUrl(originalUrl + "a".repeat(Constants.URL_MAX_LENGTH)));
  }
}

