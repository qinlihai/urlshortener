package com.ebirdspace.urlshortenerservice.service;


import com.ebirdspace.urlshortenerservice.dto.StatisticsKafkaMessage;
import com.ebirdspace.urlshortenerservice.dto.UrlDTO;
import com.ebirdspace.urlshortenerservice.mapper.UrlMapper;
import com.ebirdspace.urlshortenerservice.model.Url;
import com.ebirdspace.urlshortenerservice.repository.UrlRepository;
import com.ebirdspace.urlshortenerservice.util.UrlUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UrlService {

  private final UrlRepository urlRepository;
  private final UrlMapper urlMapper;
  private final KafkaProducer kafkaProducer;
  private final UrlUtil urlUtil;

  @Value("urlshortener.shorturl.host")
  private String shortUrlHost;

  public UrlDTO shortenUrl(String originalUrl) {
    if(!urlUtil.isValidUrl(originalUrl)) {
      return null;
    }
    Optional<Url> existingUrl = urlRepository.findByOriginalUrl(originalUrl);
    UrlDTO urlDTO;
    StatisticsKafkaMessage kafkaMessage;
    if(existingUrl.isPresent()) {
      urlDTO = urlMapper.urlToUrlDTO(existingUrl.get());
      kafkaMessage = new StatisticsKafkaMessage(urlDTO.getOriginalUrl(), urlDTO.getShortUrl(), existingUrl.get().getShortCode());
    } else {
      Url url = new Url();
      url.setOriginalUrl(originalUrl);
      url.setShortCode(urlUtil.generateShortCode());
      url.setShortUrl(shortUrlHost + "/" + url.getShortCode());
      urlRepository.save(url);
      urlDTO = urlMapper.urlToUrlDTO(url);
      kafkaMessage = new StatisticsKafkaMessage(urlDTO.getOriginalUrl(), urlDTO.getShortUrl(), url.getShortCode());
    }
    kafkaProducer.sendMessage(kafkaMessage);
    return urlDTO;
  }

  public Optional<Url> getOriginalUrl(String shortCode) {
    Optional<Url> url = urlRepository.findByShortCode(shortCode);
    url.ifPresent(u -> {
      u.setClickCount(u.getClickCount() + 1);
      urlRepository.save(u);
      StatisticsKafkaMessage kafkaMessage = new StatisticsKafkaMessage(u.getOriginalUrl(), u.getShortUrl(), u.getShortCode());

      kafkaProducer.sendMessage(kafkaMessage);
    });
    return url;
  }

}

