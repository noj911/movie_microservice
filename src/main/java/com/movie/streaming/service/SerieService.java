package com.movie.streaming.service;

import com.movie.streaming.dto.EpisodeDto;
import com.movie.streaming.dto.SaisonDto;
import com.movie.streaming.dto.SerieDto;
import com.movie.streaming.dto.SerieFilter;
import com.movie.streaming.entity.Episode;
import com.movie.streaming.entity.Saison;
import com.movie.streaming.entity.Serie;
import com.movie.streaming.entity.VideoMetadata;
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

import java.time.LocalDateTime;
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

    /**
     * Associates a VideoMetadata with an episode
     * 
     * @param serieId The ID of the serie
     * @param saisonNumero The number of the season
     * @param episodeNumero The number of the episode
     * @param metadata The VideoMetadata to associate
     * @return The updated serie
     */
    public Serie associerVideoMetadataAEpisode(String serieId, int saisonNumero, int episodeNumero, VideoMetadata metadata) {
        log.info("Associating VideoMetadata with episode: serie={}, saison={}, episode={}", 
                serieId, saisonNumero, episodeNumero);

        // Find the serie
        Serie serie = serieRepository.findById(serieId)
                .orElseThrow(() -> new SerieNotFoundException("Série non trouvée: " + serieId));

        // Find the season
        Saison saison = serie.getSaisons().stream()
                .filter(s -> s.getNumeroSequence() == saisonNumero)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Saison non trouvée: " + saisonNumero));

        // Find the episode
        Episode episode = saison.getEpisodes().stream()
                .filter(e -> e.getNumeroSequence() == episodeNumero)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Épisode non trouvé: " + episodeNumero));

        // Set modification date
       // metadata.setDateModification(LocalDateTime.now());

        // Associate the metadata
        episode.setContent(metadata);

        // Save the updated serie
        Serie updatedSerie = serieRepository.save(serie);
        log.info("VideoMetadata associated successfully : {}", metadata);

        return updatedSerie;
    }

    /**
     * Updates an episode's VideoMetadata
     * 
     * @param serieId The ID of the serie
     * @param saisonNumero The number of the season
     * @param episodeNumero The number of the episode
     * @param updatedMetadata The updated VideoMetadata
     * @return The updated serie
     */
    public Serie updateEpisodeVideoMetadata(String serieId, int saisonNumero, int episodeNumero, VideoMetadata updatedMetadata) {
        log.info("Updating VideoMetadata for episode: serie={}, saison={}, episode={}, qualities={}", 
                serieId, saisonNumero, episodeNumero, updatedMetadata.getQualitesDisponibles());

        // Find the serie
        Serie serie = serieRepository.findById(serieId)
                .orElseThrow(() -> new SerieNotFoundException("Série non trouvée: " + serieId));

        // Find the season
        Saison saison = serie.getSaisons().stream()
                .filter(s -> s.getNumeroSequence() == saisonNumero)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Saison non trouvée: " + saisonNumero));

        // Find the episode
        Episode episode = saison.getEpisodes().stream()
                .filter(e -> e.getNumeroSequence() == episodeNumero)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Épisode non trouvé: " + episodeNumero));

        // Check if the episode already has a VideoMetadata
        if (episode.getContent() == null) {
            throw new RuntimeException("L'épisode n'a pas de contenu vidéo associé");
        }

        // Update the metadata
        episode.setContent(updatedMetadata);

        // Save the updated serie
        Serie updatedSerie = serieRepository.save(serie);
        log.info("VideoMetadata updated successfully {}", updatedMetadata);

        return updatedSerie;
    }

    /**
     * Gets the VideoMetadata for an episode
     * 
     * @param serieId The ID of the serie
     * @param saisonNumero The number of the season
     * @param episodeNumero The number of the episode
     * @return The VideoMetadata for the episode
     */
    public VideoMetadata getEpisodeVideoMetadata(String serieId, int saisonNumero, int episodeNumero) {
        log.info("Getting VideoMetadata for episode: serie={}, saison={}, episode={}", 
                serieId, saisonNumero, episodeNumero);

        // Find the serie
        Serie serie = serieRepository.findById(serieId)
                .orElseThrow(() -> new SerieNotFoundException("Série non trouvée: " + serieId));

        // Find the season
        Saison saison = serie.getSaisons().stream()
                .filter(s -> s.getNumeroSequence() == saisonNumero)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Saison non trouvée: " + saisonNumero));

        // Find the episode
        Episode episode = saison.getEpisodes().stream()
                .filter(e -> e.getNumeroSequence() == episodeNumero)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Épisode non trouvé: " + episodeNumero));

        // Check if the episode has a VideoMetadata
        if (episode.getContent() == null) {
            throw new RuntimeException("L'épisode n'a pas de contenu vidéo associé");
        }

        VideoMetadata metadata = episode.getContent();
        log.info("VideoMetadata found with id: {}, qualities: {}", 
                metadata, metadata.getQualitesDisponibles());

        return metadata;
    }
}
