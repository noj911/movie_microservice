package com.movie.streaming.entity;


import org.springframework.data.mongodb.core.mapping.Field;

public class Film extends Movie {
    @Field(name = "videometadata")
    private VideoMetadata content;
}
