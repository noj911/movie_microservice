package com.movie.streaming.controller;


import com.movie.streaming.entity.MovieEntity;
import com.movie.streaming.enums.CategoryEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class MovieController {
    @QueryMapping
    public List<MovieEntity> searchMovies(
            @Argument String title,
            @Argument CategoryEnum category,
            @Argument Integer duration,
            @Argument String[] genres) {
        // Logique de recherche flexible
        Specification<MovieEntity> spec = Specification.where(null);

        if (title != null) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }

        if (category != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("category"), category.getDisplayName()));
        }

        if (duration != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("duration"), duration));
        }

        return movieRepository.findAll(spec);
    }


}
