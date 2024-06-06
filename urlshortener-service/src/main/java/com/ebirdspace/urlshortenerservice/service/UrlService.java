package com.ebirdspace.urlshortenerservice.service;


import com.ebirdspace.urlshortenerservice.model.Url;
import com.ebirdspace.urlshortenerservice.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class UrlService {

  private final UrlRepository urlRepository;

  public UrlService(UrlRepository urlRepository) {
    this.urlRepository = urlRepository;
  }

  public Url shortenUrl(String originalUrl) {
    Optional<Url> existingUrl = urlRepository.findByOriginalUrl(originalUrl);
    if(existingUrl.isPresent()) {
      return existingUrl.get();
    } else {
      Url url = new Url();
      url.setOriginalUrl(originalUrl);
      url.setShortCode(generateShortCode());
      url.setShortUrl("http://localhost:8080/" + url.getShortCode());
      return urlRepository.save(url);
    }
  }

  public Optional<Url> getOriginalUrl(String shortCode) {
    Optional<Url> url = urlRepository.findByShortCode(shortCode);
    url.ifPresent(u -> {
      u.setClickCount(u.getClickCount() + 1);
      urlRepository.save(u);
    });
    return url;
  }

  private String generateShortCode() {
    String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder shortCode = new StringBuilder();
    Random random = new Random();
    for (int i = 0; i < 6; i++) {
      shortCode.append(characters.charAt(random.nextInt(characters.length())));
    }
    return shortCode.toString();
  }
}

