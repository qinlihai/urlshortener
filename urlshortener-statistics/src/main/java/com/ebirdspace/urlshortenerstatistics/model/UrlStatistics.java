package com.ebirdspace.urlshortenerstatistics.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="url_statistics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlStatistics {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String shortCode;
  private Long clickCount;
}
