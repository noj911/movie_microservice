package com.movie.streaming.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SAISON")
@Getter
@Setter
@NoArgsConstructor
public class SaisonEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le numéro de saison est obligatoire")
    @Min(value = 1, message = "Le numéro de saison doit être positif")
    @Column(name = "NUMERO")
    private Integer numero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SERIE_ID")
    private SerieEntity serie;

    @OneToMany(mappedBy = "SAISON", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EpisodeEntity> episodes = new ArrayList<>();
}
