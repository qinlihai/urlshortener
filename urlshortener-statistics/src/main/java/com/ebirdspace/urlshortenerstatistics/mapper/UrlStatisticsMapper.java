package com.ebirdspace.urlshortenerstatistics.mapper;

import com.ebirdspace.urlshortenerstatistics.dto.UrlStatisticsDTO;
import com.ebirdspace.urlshortenerstatistics.model.UrlStatistics;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UrlStatisticsMapper {
  UrlStatisticsDTO urlStatisticsToUrlStatisticsDTO(UrlStatistics urlStatistics);
}
