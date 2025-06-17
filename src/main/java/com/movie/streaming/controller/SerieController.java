package com.movie.streaming.controller;

import com.movie.streaming.entity.Serie;
import com.movie.streaming.service.SerieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SerieController {

    private final SerieService serieService;


    @QueryMapping
    public List<Serie> getAllSerie() {
        return  serieService.findAll();
    }

}
