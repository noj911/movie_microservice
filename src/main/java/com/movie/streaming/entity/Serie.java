package com.movie.streaming.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "series")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Serie extends Movie {

    @NotBlank(message = "Le nom du producteur de la série est obligatoire")
    @Size(max = 100, message = "Le nom du créateur ne peut pas dépasser 100 caractères")
    @Field(name = "producteur")
    private String producteur;

    @Field(name = "saisons")
    private List<Saison> saisons;

    @Field(name = "en_cours")
    private boolean enCours;
}
