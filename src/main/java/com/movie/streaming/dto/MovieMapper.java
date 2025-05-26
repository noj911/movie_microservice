package com.movie.streaming.dto;
import com.movie.streaming.entity.MovieEntity;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface MovieMapper {
    MovieDto toDto(MovieEntity entity);
    MovieEntity toEntity(MovieDto dto);
}
