package com.movie.streaming.mapper;
import com.movie.streaming.dto.MovieDto;
import com.movie.streaming.entity.Movie;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface MovieMapper {
    MovieDto toDto(Movie entity);
    Movie toEntity(MovieDto dto);
}
