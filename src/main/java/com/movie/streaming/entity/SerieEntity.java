package com.movie.streaming.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("SERIE")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class SerieEntity extends MovieEntity {

    @NotBlank(message = "Le créateur est obligatoire")
    @Size(max = 100, message = "Le nom du créateur ne peut pas dépasser 100 caractères")
    @Column(name = "CREATEUR", length = 100)
    private String createur;

    @OneToMany(mappedBy = "SERIE", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaisonEntity> saisons = new ArrayList<>();

    @Column(name = "EN_COURS")
    private boolean enCours = true;
}
