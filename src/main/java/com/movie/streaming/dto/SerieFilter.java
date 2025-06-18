package com.movie.streaming.dto;

import lombok.Data;

@Data
public class SerieFilter {
    private String title;
    private String producteur;
    private String category;
    private Boolean enCours;

}
