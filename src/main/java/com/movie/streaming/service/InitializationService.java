package com.movie.streaming.service;

import com.movie.streaming.entity.Episode;
import com.movie.streaming.entity.Saison;
import com.movie.streaming.entity.Serie;
import com.movie.streaming.repository.SerieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InitializationService {


    private final SerieRepository serieRepository;

    @PostConstruct
    public void initialization() {
        // Vérifier si la collection est vide avant d'initialiser
        if (serieRepository.count() == 0) {
            List<Serie> listSerie = Arrays.asList(
                    Serie.builder()
                            .producteur("Hollywood")
                            .enCours(true)
                            .title("Breaking Bad")
                            .saisons(Arrays.asList(
                                            Saison.builder()
                                                    .titre("Saison1")
                                                    .numero(1).
                                                    episodes(Collections.singletonList(
                                                            Episode.builder()
                                                                    .titre("Intro")
                                                                    .numeroSequence(1)
                                                                    .duree(50)
                                                                    .description("Description de l'episode 1")
                                                                    .build()
                                                    )).build()

                            )

                            )
                            .build()

            );


            serieRepository.saveAll(listSerie);
        }

    }

}
