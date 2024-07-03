package com.ebirdspace.urlshortenerservice.mapper;

import com.ebirdspace.urlshortenerservice.dto.UrlDTO;
import com.ebirdspace.urlshortenerservice.model.Url;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UrlMapper {
  UrlDTO urlToUrlDTO(Url url);
}
