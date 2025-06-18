package com.movie.streaming.service;

import com.movie.streaming.dto.SerieDto;
import com.movie.streaming.dto.SerieFilter;
import com.movie.streaming.entity.Serie;

import java.util.List;

public interface ISerieService {

    List<Serie> findAll();

    List<Serie> getSeriesByCriteria(SerieFilter filter);

    List<Serie> findSerieByCategory(String category);

    Serie save(SerieDto serieInput);

    Serie update(String id, SerieDto serieInput);
}
