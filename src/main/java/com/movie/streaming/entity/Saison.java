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
import java.util.List;

@Document(collection = "saison")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Saison implements Serializable {


    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 100, message = "Le titre ne peut pas dépasser 100 caractères")
    @Field(name = "titre")
    private String titre;

    @NotNull(message = "Le numéro de saison est obligatoire")
    @Min(value = 1, message = "Le numéro de saison doit être positif")
    @Field(name = "numeroSequence")
    private Integer numeroSequence;

    @Field(name = "episode")
    private List<Episode> episodes;


}
