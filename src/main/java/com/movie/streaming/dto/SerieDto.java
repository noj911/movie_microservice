package com.movie.streaming.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
public class SerieDto extends MovieDto {

    private String producteur;

    private List<SaisonDto> saisons;

    private boolean enCours;



}
