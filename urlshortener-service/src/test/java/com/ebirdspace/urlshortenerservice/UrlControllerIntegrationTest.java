package com.ebirdspace.urlshortenerservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.ebirdspace.urlshortenerservice.model.Url;
import com.ebirdspace.urlshortenerservice.repository.UrlRepository;
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
public class UrlControllerIntegrationTest {

  static Logger log = LoggerFactory.getLogger(UrlControllerIntegrationTest.class);

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UrlRepository urlRepository;

  @Container
  public static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.26")
      .withDatabaseName("test_db")
      .withUsername("test")
      .withPassword("test");

  @Test
  public void testPostEndpoint() throws Exception {
    String jsonRequest1 = "{\"originalUrl\": \"www.yahoo.com\"}";
    String jsonRequest2 = "{\"originalUrl\": \"ttt://www.yahoo.com\"}";

    mockMvc.perform(post("/api/v1/shorten")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonRequest1))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.shortUrl").isNotEmpty())
        .andExpect(jsonPath("$.originalUrl").isNotEmpty())
        .andExpect(jsonPath("$.originalUrl").value("www.yahoo.com"));

    mockMvc.perform(post("/api/v1/shorten")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonRequest2))
        .andExpect(status().isBadRequest());

    Optional<Url> urlOptional = urlRepository.findByOriginalUrl("www.yahoo.com");
    assertTrue(urlOptional.isPresent());
    String shortCode = urlOptional.get().getShortCode();

    mockMvc.perform(get("/" + shortCode)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isFound());

    mockMvc.perform(get("/badshortcode")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());

  }


}
