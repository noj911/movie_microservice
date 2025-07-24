package com.movie.streaming.entity;

import com.movie.streaming.enums.CategoryEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serial;
import java.io.Serializable;

/**
 * Movie Entity
 */
@Document(collection = "movie")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder

public abstract class Movie implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotBlank(message = "Le titre est obligatoire")
    @Field(name = "title")
    private String title;

    @Min(value = 1, message = "La durée doit être positive")
    @Field(name = "duration")
    private Integer duration; // durée en minutes

    @NotBlank(message = "Le genre est obligatoire")
    private CategoryEnum category;

    @Field(name = "thumbnail_url")
    private String thumbnailUrl;

    @Field(name = "SYNOPSIS")
    private String synopsis;
}
