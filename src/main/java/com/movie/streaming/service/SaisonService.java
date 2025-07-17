package com.movie.streaming.service;

import com.movie.streaming.dto.SerieDto;
import com.movie.streaming.dto.SerieFilter;
import com.movie.streaming.entity.Saison;
import com.movie.streaming.mapper.SerieMapper;
import com.movie.streaming.repository.SerieRepository;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

public class SaisonService implements ISaisonService {

    @Override
    public List<Saison> findAll() {
        return List.of();
    }

    @Override
    public List<Saison> getSeriesByCriteria(SerieFilter filter) {
        return List.of();
    }

    @Override
    public List<Saison> findSerieByCategory(String category) {
        return List.of();
    }

    @Override
    public Saison save(SerieDto serieInput) {
        return null;
    }

    @Override
    public boolean delete(SerieDto serieInput) {
        return false;
    }

    @Override
    public Saison update(SerieDto serieInput) {
        return null;
    }
}
