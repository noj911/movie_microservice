package com.movie.streaming.service;

import com.movie.streaming.entity.Serie;
import com.movie.streaming.repository.SerieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@RequiredArgsConstructor
@Service
@Slf4j
public class SerieService {

    private  final SerieRepository serieRepository;

    public List<Serie> findAll() {
        return serieRepository.findAll();
    }

}
