package com.ebirdspace.urlshortenerservice.controller;

import com.ebirdspace.urlshortenerservice.model.Url;
import com.ebirdspace.urlshortenerservice.service.KafkaProducer;
import com.ebirdspace.urlshortenerservice.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@Tag(name = "Url Shortener", description = "Url Shortener APIs")
@CrossOrigin(origins="*", allowedHeaders = "*")
@RestController
public class UrlController {

  private final UrlService urlService;

  private final KafkaProducer kafkaProducer;

  public UrlController(UrlService urlService, KafkaProducer kafkaProducer) {
    this.urlService = urlService;
    this.kafkaProducer = kafkaProducer;
  }

  @Operation(
      summary = "Shorten a given url",
      description = "Shorten a url by specifying the original url")
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Url.class), mediaType = "application/json") })
  })
  @PostMapping("/api/v1/shorten")
  public ResponseEntity<Url> shortenUrl(@RequestBody String originalUrl) {
    Url url = urlService.shortenUrl(originalUrl);
    kafkaProducer.sendMessage("Shorten url: " + originalUrl);
    return ResponseEntity.ok(url);
  }

  @Operation(
      summary = "Redirect the short url to the original url",
      description = "Redirect the short url to the original url by specifying the short code")
  @ApiResponses({
      @ApiResponse(responseCode = "302", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })
  })
  @GetMapping("/{shortCode}")
  public ResponseEntity<Void> redirectUrl(@PathVariable String shortCode) {
    Optional<Url> urlOptional = urlService.getOriginalUrl(shortCode);
    if (urlOptional.isPresent()) {
      String originalUrl = urlOptional.get().getOriginalUrl().toLowerCase();
      if(!originalUrl.contains("http:") && !originalUrl.contains("https:")) {
        originalUrl = "http://" + originalUrl;
      }
      kafkaProducer.sendMessage("Redirect url: " + shortCode + " to " + originalUrl);
      return ResponseEntity.status(302).location(URI.create(originalUrl)).build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
