package com.movie.streaming.mapper;

import com.movie.streaming.dto.SerieDto;
import com.movie.streaming.entity.Serie;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface SerieMapper {
    Serie toEntity(SerieDto serieInput);
    SerieDto toDto(Serie serie);


}
