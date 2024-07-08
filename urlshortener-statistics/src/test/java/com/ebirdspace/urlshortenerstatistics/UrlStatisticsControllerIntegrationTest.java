package com.ebirdspace.urlshortenerstatistics;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.ebirdspace.urlshortenerstatistics.model.UrlStatistics;
import com.ebirdspace.urlshortenerstatistics.repository.UrlStatisticsRepository;
import java.util.Optional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.Assert.*;

//@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Testcontainers
@EmbeddedKafka(topics = "${kafka.urlshortener.topic-name}")
@DirtiesContext
public class UrlStatisticsControllerIntegrationTest {

  static Logger log = LoggerFactory.getLogger(UrlStatisticsControllerIntegrationTest.class);

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UrlStatisticsRepository urlStatisticsRepository;

  @Container
  public static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.26")
      .withDatabaseName("test_db")
      .withUsername("test")
      .withPassword("test");

  @Test
  public void testPostEndpoint() throws Exception {

    String shortCode = "4t3ZEi";
    UrlStatistics urlStatistics = new UrlStatistics();
    urlStatistics.setShortCode(shortCode);
    urlStatistics.setClickCount(1L);
    urlStatisticsRepository.save(urlStatistics);

    String jsonRequest1 = "{\"shortCode\": \"" + shortCode + "\"}";
    String jsonRequest2 = "{\"shortCode\": \"badcode\"}";

    mockMvc.perform(post("/api/v1/urlstatistics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonRequest1))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.shortCode").value(shortCode))
        .andExpect(jsonPath("$.clickCount").isNotEmpty());

    mockMvc.perform(post("/api/v1/urlstatistics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonRequest2))
        .andExpect(status().isNotFound());

    mockMvc.perform(get("/api/v1/urlstatistics")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray());

  }


}

