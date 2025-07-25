package com.movie.streaming.mapper;

import com.movie.streaming.dto.SaisonDto;
import com.movie.streaming.entity.Saison;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SaisonMapper {
    Saison toDto(SaisonDto saisonInput);
    SaisonDto toEntity(Saison saison);

}
