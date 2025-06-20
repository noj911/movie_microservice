package com.movie.streaming.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "content")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class MovieContent {
    private String nomFichier;
    private String cheminStockage;
    private String mimeType;
    private long tailleFichier;

    // Informations de streaming
    private String urlStreaming;
    private List<QualiteStream> qualitesDisponibles;

    // Métadonnées techniques
    private String codec;
    private String bitrateVideo;
    private String bitrateAudio;

    // État du contenu
    private StatutTraitement statut;
    private LocalDateTime dateUpload;

}
