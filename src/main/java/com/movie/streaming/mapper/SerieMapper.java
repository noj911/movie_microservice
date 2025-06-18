package com.movie.streaming.mapper;

import com.movie.streaming.dto.SerieDto;
import com.movie.streaming.entity.Serie;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SerieMapper {
    Serie serieInputToSerie(SerieDto serieInput);
    SerieDto serieToSerieDto(Serie serie);
    void updateSerieFromInput(@MappingTarget Serie serie, SerieDto serieInput);


}
