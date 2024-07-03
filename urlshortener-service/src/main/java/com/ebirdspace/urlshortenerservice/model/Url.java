package com.ebirdspace.urlshortenerservice.model;

import com.ebirdspace.urlshortenerservice.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Url {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(length = 2048)
  private String originalUrl;
  private String shortUrl;
  private Long clickCount = 0L;

  @Column(nullable = false, updatable = false, unique = true)
  private String shortCode;
}

