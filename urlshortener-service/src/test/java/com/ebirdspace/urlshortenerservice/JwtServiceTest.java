package com.ebirdspace.urlshortenerservice;

import com.ebirdspace.urlshortenerservice.model.User;
import com.ebirdspace.urlshortenerservice.repository.UrlRepository;
import com.ebirdspace.urlshortenerservice.repository.UserRepository;
import com.ebirdspace.urlshortenerservice.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
// Disable DataSource and related auto configuration so that DB connection is not established
// in the test Spring Data abstraction to access the DB, UserRepository is mocked for the
// purpose of this testing anyways
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class,
    KafkaAutoConfiguration.class})
public class JwtServiceTest {

  @Autowired
  private JwtService jwtService;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private UrlRepository urlRepository;

  private User user;

  @BeforeEach
  void init() {
    user = new User();
    user.setEmail("tmp@gmail.com");
    user.setPassword("changemenow");
  }


  @Test
  public void testGenerateToken() {
    String token = jwtService.generateToken(user);
    assertNotNull(token);
  }

  @Test
  public void testExtractUsername() {
    String token = jwtService.generateToken(user);
    assertEquals("tmp@gmail.com", jwtService.extractUsername(token));
  }

  @Test
  public void testValidateToken() {
    String token = jwtService.generateToken(user);
    assertTrue(jwtService.isTokenValid(token, user));
  }
}

