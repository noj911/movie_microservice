package com.movie.streaming.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = "saison")
@Getter
@Setter
@NoArgsConstructor
public class Saison implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 100, message = "Le titre ne peut pas dépasser 100 caractères")
    @Field(name = "titre")
    private String titre;

    @NotNull(message = "Le numéro de saison est obligatoire")
    @Min(value = 1, message = "Le numéro de saison doit être positif")
    @Field(name = "numero")
    private Integer numero;

    @Field(name = "episode")
    private List<Episode> episodes = new ArrayList<>();
}
