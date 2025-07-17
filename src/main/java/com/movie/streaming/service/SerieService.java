package com.movie.streaming.service;

import com.movie.streaming.dto.EpisodeDto;
import com.movie.streaming.dto.SaisonDto;
import com.movie.streaming.dto.SerieDto;
import com.movie.streaming.dto.SerieFilter;
import com.movie.streaming.entity.Saison;
import com.movie.streaming.entity.Serie;
import com.movie.streaming.enums.CategoryEnum;
import com.movie.streaming.exception.SerieNotFoundException;
import com.movie.streaming.mapper.EpisodeMapper;
import com.movie.streaming.mapper.SaisonMapper;
import com.movie.streaming.mapper.SerieMapper;
import com.movie.streaming.repository.SerieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
@Slf4j
public class SerieService implements ISerieService{
    private final SerieMapper serieMapper;
    private final SaisonMapper saisonMapper;
    private final EpisodeMapper episodeMapper;
    private  final SerieRepository serieRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public List<Serie> findAll() {
        return serieRepository.findAll();
    }
    @Override
    public List<Serie> getSeriesByCriteria(SerieFilter filter){
        Criteria criteria = new Criteria();

        // Construction dynamique des critères de recherche
        if (filter != null) {
            if (filter.getTitle() != null && !filter.getTitle().isEmpty()) {
                criteria.and("titre").regex(filter.getTitle(), "i"); // "i" pour recherche insensible à la casse
            }

            if (filter.getProducteur() != null && !filter.getProducteur().isEmpty()) {
                criteria.and("producteur").regex(filter.getProducteur(), "i");
            }

            if (filter.getCategory() != null && !filter.getCategory().isEmpty()) {
                criteria.and("categorie").regex(filter.getCategory(), "i");
            }

            if (filter.getEnCours() != null) {
                criteria.and("enCours").is(filter.getEnCours());
            }
        }

        return mongoTemplate.find(Query.query(criteria), Serie.class);
    }

    @Override
    public List<Serie> findSerieByCategory(String category) {
        return serieRepository.getSeriesByCategory(CategoryEnum.valueOf(category));
    }

    @Override
    public Serie save(SerieDto serieInput) {
        Serie serie = serieMapper.toEntity(serieInput);
        return serieRepository.save(serie);
    }
    @Override
    public boolean delete(SerieDto serieInput) {
        Serie serie = serieMapper.toEntity(serieInput);
         serieRepository.deleteById(serie.getId());
         return true;
    }


    @Override
    public Serie update(SerieDto serieInput) {
        Optional<Serie> existingSerie = serieRepository.findById(String.valueOf(serieInput.getId()));
        if (existingSerie.isPresent()) {
            Serie serietoUpdate= serieMapper.toEntity(serieInput);
            return serieRepository.save(serietoUpdate);
        }else {
            throw new SerieNotFoundException("Serie non retrouvée: " + serieInput.getId());
        }
    }
    @Override
    public Serie ajouterSaison(String idSerie, SaisonDto nouvelleSaison) {
        Serie serie = serieRepository.findById(idSerie)
                .orElseThrow(() -> new RuntimeException("Série non trouvée"));
        Saison newSaison = saisonMapper.toDto(nouvelleSaison);
        serie.getSaisons().add(newSaison);
        return serieRepository.save(serie);
    }
    @Override
    public Serie ajouterEpisode(String idSerie, int numeroSaison, EpisodeDto nouvelEpisode) {
        Serie serie = serieRepository.findById(idSerie)
                .orElseThrow(() -> new RuntimeException("Série non trouvée"));

        Saison saison = serie.getSaisons().stream()
                .filter(s -> s.getNumeroSequence() == numeroSaison)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Saison non trouvée"));
        saison.getEpisodes().add(episodeMapper.toEntity(nouvelEpisode));
        return serieRepository.save(serie);
    }
}
