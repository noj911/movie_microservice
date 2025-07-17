package com.movie.streaming.mapper;

import com.movie.streaming.dto.EpisodeDto;
import com.movie.streaming.entity.Episode;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EpisodeMapper {
    EpisodeDto toDto(Episode entity);
    Episode toEntity(EpisodeDto dto);

}
