package com.ebirdspace.urlshortenerservice.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

  /**
   * This integrates CORS configuration with Spring Security configuration
   * @return
   */
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOrigins(List.of("*"));
    configuration.setAllowedMethods(List.of("GET","POST", "PUT", "DELETE", "OPTIONS"));
//    configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    source.registerCorsConfiguration("/**",configuration);

    return source;
  }

}

