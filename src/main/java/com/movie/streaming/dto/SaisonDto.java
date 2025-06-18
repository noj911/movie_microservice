package com.movie.streaming.dto;

import lombok.Data;

import java.util.List;
@Data
public class SaisonDto {
    private String titre;
    private Integer numeroSequence;
    private List<EpisodeDto> episodes;
}
