package com.movie.streaming.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "EPISODE")
@Getter
@Setter
@NoArgsConstructor
public class EpisodeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 100, message = "Le titre ne peut pas dépasser 100 caractères")
    @Column(name = "TITRE", length = 100)
    private String titre;

    @NotNull(message = "Le numéro d'épisode est obligatoire")
    @Min(value = 1, message = "Le numéro d'épisode doit être positif")
    @Column(name = "NUMERO")
    private Integer numero;

    @NotNull(message = "La durée est obligatoire")
    @Min(value = 1, message = "La durée doit être positive")
    @Column(name = "DUREE")
    private Integer duree; // en minutes

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Column(name = "URL_MINIATURE")
    private String urlMiniature;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SAISON_ID")
    private SaisonEntity saison;
}

