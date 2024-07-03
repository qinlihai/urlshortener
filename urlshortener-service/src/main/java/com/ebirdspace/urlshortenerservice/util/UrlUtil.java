package com.ebirdspace.urlshortenerservice.util;

import java.util.Random;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class UrlUtil {

  private static final String URL_REGEX = "^((https?|ftp|file)://)?(www\\.)?([a-zA-Z0-9]+)\\.[a-z]+(/\\S*)?$";
  private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

  public boolean isValidUrl(String url) {
    if (url == null) {
      return false;
    }
    return URL_PATTERN.matcher(url).matches();
  }

  public String generateShortCode() {
    String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder shortCode = new StringBuilder();
    Random random = new Random();
    for (int i = 0; i < Constants.SHORTCODE_LENGTH; i++) {
      shortCode.append(characters.charAt(random.nextInt(characters.length())));
    }
    return shortCode.toString();
  }
}

