package com.movie.streaming.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Document(collection = "episode")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Episode implements Serializable {

    private static final long serialVersionUID = 1L;
    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 100, message = "Le titre ne peut pas dépasser 100 caractères")
    @Field(name = "titre")
    private String titre;

    @NotNull(message = "Le numéro d'épisode est obligatoire")
    @Min(value = 1, message = "Le numéro d'épisode doit être positif")
    @Field(name = "numerosequence")
    private Integer numeroSequence;

    @NotNull(message = "La durée est obligatoire")
    @Min(value = 1, message = "La durée doit être positive")
    @Field(name = "duree")
    private Integer duree; // en minutes

    @Field(name = "description")
    private String description;

    @Field(name = "url_miniature")
    private String urlMiniature;

    @Field(name = "saisonId")
    private String saisonId;


}

