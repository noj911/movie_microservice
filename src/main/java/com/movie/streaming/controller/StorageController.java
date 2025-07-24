package com.movie.streaming.controller;

import com.movie.streaming.entity.VideoMetadata;
import com.movie.streaming.service.S3StorageService;
import com.movie.streaming.service.SerieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * REST controller for file storage operations
 */
@RestController
@RequestMapping("/api/storage")
@RequiredArgsConstructor
@Slf4j
public class StorageController {

    private final S3StorageService s3StorageService;
    private final SerieService serieService;

    /**
     * Uploads a file to S3
     * 
     * @param file The file to upload
     * @param qualite The quality of the video (SD_480P, HD_720P, etc.)
     * @return The metadata of the uploaded file
     */
    @PostMapping("/upload")
    public ResponseEntity<VideoMetadata> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "qualite", defaultValue = "HD_720P") String qualite) {

        try {
            log.info("Received file upload request: {}, quality: {}", file.getOriginalFilename(), qualite);
            VideoMetadata metadata = s3StorageService.uploadFile(file, qualite);
            return ResponseEntity.ok(metadata);
        } catch (IOException e) {
            log.error("Error uploading file", e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Uploads a file and associates it with an episode
     * 
     * @param file The file to upload
     * @param serieId The ID of the serie
     * @param saisonNumero The number of the season
     * @param episodeNumero The number of the episode
     * @param qualite The quality of the video
     * @return The updated serie
     */
    @PostMapping("/upload/episode")
    public ResponseEntity<?> uploadEpisodeContent(
            @RequestParam("file") MultipartFile file,
            @RequestParam("serieId") String serieId,
            @RequestParam("saisonNumero") int saisonNumero,
            @RequestParam("episodeNumero") int episodeNumero,
            @RequestParam(value = "qualite", defaultValue = "HD_720P") String qualite) {

        try {
            log.info("Received episode content upload request: serie={}, saison={}, episode={}, quality={}",
                    serieId, saisonNumero, episodeNumero, qualite);

            // 1. Upload the file to S3
            VideoMetadata metadata = s3StorageService.uploadFile(file, qualite);

            // 2. Associate the metadata with the episode
            serieService.associerVideoMetadataAEpisode(serieId, saisonNumero, episodeNumero, metadata);

            return ResponseEntity.ok(metadata);
        } catch (Exception e) {
            log.error("Error uploading episode content", e);
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Deletes a file from S3
     * 
     * @param s3Path The path of the file in S3
     * @return 200 OK if successful
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFile(@RequestParam("path") String s3Path) {
        log.info("Received file delete request: {}", s3Path);
        s3StorageService.deleteFile(s3Path);
        return ResponseEntity.ok().build();
    }

    /**
     * Gets the URL for accessing a file
     * 
     * @param s3Path The path of the file in S3
     * @return The URL for accessing the file
     */
    @GetMapping("/url")
    public ResponseEntity<String> getUrl(@RequestParam("path") String s3Path) {
        log.info("Received URL generation request: {}", s3Path);
        String url = s3StorageService.generateUrl(s3Path);
        return ResponseEntity.ok(url);
    }

    /**
     * Adds a new quality version to an existing episode's video
     * 
     * @param file The file to upload
     * @param serieId The ID of the serie
     * @param saisonNumero The number of the season
     * @param episodeNumero The number of the episode
     * @param qualite The quality of the video
     * @return The updated VideoMetadata
     */
    @PostMapping("/upload/episode/quality")
    public ResponseEntity<?> addEpisodeQuality(
            @RequestParam("file") MultipartFile file,
            @RequestParam("serieId") String serieId,
            @RequestParam("saisonNumero") int saisonNumero,
            @RequestParam("episodeNumero") int episodeNumero,
            @RequestParam("qualite") String qualite) {

        try {
            log.info("Received request to add quality {} for episode: serie={}, saison={}, episode={}",
                    qualite, serieId, saisonNumero, episodeNumero);

            // 1. Get the existing VideoMetadata for the episode
            VideoMetadata existingMetadata = serieService.getEpisodeVideoMetadata(serieId, saisonNumero, episodeNumero);

            // 2. Add the new quality to the VideoMetadata
            VideoMetadata updatedMetadata = s3StorageService.addQualite(file, existingMetadata, qualite);

            // 3. Update the episode with the new VideoMetadata
            serieService.updateEpisodeVideoMetadata(serieId, saisonNumero, episodeNumero, updatedMetadata);

            return ResponseEntity.ok(updatedMetadata);
        } catch (Exception e) {
            log.error("Error adding quality to episode", e);
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
