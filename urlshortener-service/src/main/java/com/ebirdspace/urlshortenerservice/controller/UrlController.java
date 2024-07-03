package com.ebirdspace.urlshortenerservice.controller;

import com.ebirdspace.urlshortenerservice.dto.UrlDTO;
import com.ebirdspace.urlshortenerservice.model.Url;
import com.ebirdspace.urlshortenerservice.dto.UrlShortenRequest;
import com.ebirdspace.urlshortenerservice.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@Tag(name = "Url Shortener", description = "Url Shortener APIs")
@RequiredArgsConstructor
@RestController
public class UrlController {

  private final UrlService urlService;

  @Operation(
      summary = "Shorten a given url",
      description = "Shorten a url by specifying the original url")
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Url.class), mediaType = "application/json") })
  })
  @PostMapping("/api/v1/shorten")
  public ResponseEntity<UrlDTO> shortenUrl(@RequestBody UrlShortenRequest urlShortenRequest) {
    UrlDTO urlDTO = urlService.shortenUrl(urlShortenRequest.getOriginalUrl());
    if(urlDTO == null) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok(urlDTO);
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
      String originalUrl = urlOptional.get().getOriginalUrl();
      if(!originalUrl.contains("http:") && !originalUrl.contains("https:")) {
        originalUrl = "http://" + originalUrl;
      }
      return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalUrl)).build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
