package com.ebirdspace.urlshortenerservice;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.ebirdspace.urlshortenerservice.repository.UrlRepository;
import com.ebirdspace.urlshortenerservice.service.KafkaProducer;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.State;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest
// Disable DataSource and related auto configuration so that DB connection is not established
// in the test Spring Data abstraction to access the DB, UserRepository is mocked for the
// purpose of this testing anyways
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class,
    KafkaAutoConfiguration.class})
// Since CircuitBreaker state is shared between the threads and each of tests in this class
// relies on specific state, we don't allow to run these tests in parallel
@Execution(ExecutionMode.SAME_THREAD)
public class KafkaProducerTest {

  private static final Logger logger = LoggerFactory.getLogger(KafkaProducerTest.class);

  @Autowired
  private KafkaProducer kafkaProducer;

  @Autowired
  private CircuitBreakerRegistry circuitBreakerRegistry;

  private CircuitBreaker circuitBreaker;

  @MockBean
  private KafkaTemplate<String, Object> kafkaTemplate;

  @MockBean
  private UrlRepository urlRepository;

  @MockBean
  private KafkaAdmin kafkaAdmin;

  @BeforeEach
  void setUp() {
    circuitBreaker = circuitBreakerRegistry.circuitBreaker("kafkaProducer");
  }

  @Test
  public void testCircuitBreaker() {
    CircuitBreaker.State stateBefore = circuitBreaker.getState();

    when(kafkaTemplate.send(any(), any())).thenReturn(null);
    kafkaProducer.sendMessage("my message");

    CircuitBreaker.State stateAfter = circuitBreaker.getState();
    assertTrue(stateBefore.equals(CircuitBreaker.State.CLOSED));
    assertTrue(stateAfter.equals(CircuitBreaker.State.CLOSED));

    doThrow(new RuntimeException("simulation failure")).when(kafkaTemplate).send(any(), any());

    assertThrows(RuntimeException.class, () -> kafkaProducer.sendMessage("my message"));

    assertThrows(RuntimeException.class, () -> kafkaProducer.sendMessage("my message"));
    assertThrows(CallNotPermittedException.class, () -> kafkaProducer.sendMessage("my message"));
    stateAfter = circuitBreaker.getState();
    assertTrue(stateAfter.equals(State.OPEN));

  }
}
