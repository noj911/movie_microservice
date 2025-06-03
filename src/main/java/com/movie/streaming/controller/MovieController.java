package com.movie.streaming.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@AllArgsConstructor
@Controller
public class MovieController {
    @QueryMapping
    public String hello() {
        return "Bonjour tout le monde!";
    }



}
