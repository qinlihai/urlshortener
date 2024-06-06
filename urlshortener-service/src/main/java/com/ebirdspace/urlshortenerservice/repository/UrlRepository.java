package com.ebirdspace.urlshortenerservice.repository;



import com.ebirdspace.urlshortenerservice.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {
  Optional<Url> findByShortCode(String shortCode);
  Optional<Url> findByOriginalUrl(String originalUrl);
}

