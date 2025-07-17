package com.movie.streaming.service;

import com.movie.streaming.dto.SerieDto;
import com.movie.streaming.dto.SerieFilter;
import com.movie.streaming.entity.Saison;
import com.movie.streaming.entity.Serie;

import java.util.List;

public interface ISaisonService {
    List<Saison> findAll();

    List<Saison> getSeriesByCriteria(SerieFilter filter);

    List<Saison> findSerieByCategory(String category);

    Saison save(SerieDto serieInput);

    boolean delete(SerieDto serieInput);

    Saison update( SerieDto serieInput);
}
