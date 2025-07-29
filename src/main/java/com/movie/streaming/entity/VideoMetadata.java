package com.movie.streaming.entity;

import com.movie.streaming.enums.QualiteStream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "content")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class VideoMetadata {
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
    private LocalDateTime dateUpload;

}
