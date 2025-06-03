package com.movie.streaming.repository;

import com.movie.streaming.entity.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface MovieRepository extends MongoRepository<Movie, UUID>
         {


}

