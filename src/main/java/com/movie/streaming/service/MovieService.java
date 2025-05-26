package com.movie.streaming.service;

import com.movie.streaming.dto.MovieDto;
import com.movie.streaming.dto.MovieMapper;
import com.movie.streaming.dto.MovieSearchCriteria;
import com.movie.streaming.entity.MovieEntity;
import com.movie.streaming.enums.CategoryEnum;
import com.movie.streaming.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
@Slf4j

public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;


    public MovieService(MovieRepository movieRepository,MovieMapper movieMapper ) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;

    }
    public Page<MovieDto> searchMovies(MovieSearchCriteria criteria, Pageable pageable) {
        if (criteria == null) {
            throw new IllegalArgumentException("Les critères de recherche ne peuvent pas être null");
        }
        if (pageable == null) {
            throw new IllegalArgumentException("Les paramètres de pagination ne peuvent pas être null");
        }
        try {
            return movieRepository.findByFilters(criteria, pageable)
                    .map(entity -> entity != null ? movieMapper.toDto(entity) : null);
        } catch (Exception e) {
            log.error("Erreur lors de la recherche des films: {}", e.getMessage());
            throw new RuntimeException("Erreur lors de la recherche des films", e);
        }

    }
/*    public MovieDto save(MovieDto movieDto) {
        MovieEntity movie = mapToEntity(movieDto);
        MovieEntity savedMovie = movieRepository.save(movie);
        return mapToDto(savedMovie);
    }

    public List<MovieDto> findAll() {
        return movieRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Optional<MovieDto> findById(Long id) {
        return movieRepository.findById(id)
                .map(movieMapper::mapToDto);
    }*/


/*    public Page<MovieEntity> searchMovies(
            String title,
            CategoryEnum categoryId,
            Integer minDuration,
            Integer maxDuration,
            int page,
            int size,
            String sortBy,
            Sort.Direction direction) {

        MovieSearchCriteria criteria = MovieSearchCriteria.builder()
                .title(title)
                .category(categoryId)
                .minDuration(minDuration)
                .maxDuration(maxDuration)
                .build();

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(direction, sortBy)
        );

        return movieRepository.findByFilters(criteria, pageable);
    }*/



}
