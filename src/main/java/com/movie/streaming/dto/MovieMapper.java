package com.movie.streaming.dto;
import com.movie.streaming.entity.Movie;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface MovieMapper {
    MovieDto toDto(Movie entity);
    Movie toEntity(MovieDto dto);
}
