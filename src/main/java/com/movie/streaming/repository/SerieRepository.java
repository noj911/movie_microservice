package com.movie.streaming.repository;

import com.movie.streaming.entity.Serie;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SerieRepository extends MongoRepository<Serie, String> {

}
