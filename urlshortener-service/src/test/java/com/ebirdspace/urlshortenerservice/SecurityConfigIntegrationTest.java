package com.ebirdspace.urlshortenerservice;

import com.ebirdspace.urlshortenerservice.dto.LoginResponse;
import com.ebirdspace.urlshortenerservice.dto.LoginUserDTO;
import com.ebirdspace.urlshortenerservice.dto.RegisterUserDTO;
import com.ebirdspace.urlshortenerservice.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@EnableAutoConfiguration(exclude = {KafkaAutoConfiguration.class})
@Testcontainers
@DirtiesContext
public class SecurityConfigIntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Container
  public static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.26")
      .withDatabaseName("test_db")
      .withUsername("test")
      .withPassword("test");

  @Test
  public void testAuthentication() {
    LoginUserDTO loginUserDTO = new LoginUserDTO();
    loginUserDTO.setEmail("tmp@gmail.com");
    loginUserDTO.setPassword("changemenow");
    RegisterUserDTO registerUserDTO = new RegisterUserDTO();
    registerUserDTO.setEmail("tmp@gmail.com");
    registerUserDTO.setPassword("changemenow");
    registerUserDTO.setFullName("Test User");

    ResponseEntity<User> response = restTemplate.postForEntity("http://localhost:" + port + "/signup", registerUserDTO, User.class);
    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    assertNotNull(response.getBody());
    User user = response.getBody();

    ResponseEntity<LoginResponse> response2 = restTemplate.postForEntity("http://localhost:" + port + "/login", loginUserDTO, LoginResponse.class);
    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    assertNotNull(response.getBody());

    LoginResponse loginResponse = response2.getBody();
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + loginResponse.getToken());

    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<User> authResponse = restTemplate.exchange("http://localhost:" + port + "/secure/" + user.getId(), HttpMethod.POST, entity, User.class);
    assertEquals(HttpStatus.OK.value(), authResponse.getStatusCode().value());
  }
}

