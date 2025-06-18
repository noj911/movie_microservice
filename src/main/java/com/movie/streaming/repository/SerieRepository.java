package com.movie.streaming.repository;

import com.movie.streaming.entity.Serie;
import com.movie.streaming.enums.CategoryEnum;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SerieRepository extends MongoRepository<Serie, String> {
    List<Serie> getSeriesByCategory(CategoryEnum category);

    Serie getSeriesById(String id);
}
