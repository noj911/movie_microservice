package com.movie.streaming.service;

import com.movie.streaming.entity.VideoMetadata;
import com.movie.streaming.enums.QualiteStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service for handling file operations with AWS S3
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class S3StorageService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    /**
     * Uploads a file to Amazon S3 and returns the metadata
     * 
     * @param file The file to upload
     * @param qualite The quality of the video (SD_480P, HD_720P, etc.)
     * @return VideoMetadata containing information about the uploaded file
     * @throws IOException If there's an error reading the file
     */
    public VideoMetadata uploadFile(MultipartFile file, String qualite) throws IOException {
        // Generate a unique filename
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // S3 path (e.g., videos/HD_720P/myfile.mp4)
        String s3Path = "videos/" + qualite + "/" + fileName;

        log.info("Uploading file {} to S3 path: {}", fileName, s3Path);

        // Upload the file to S3
        s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Path)
                .contentType(file.getContentType())
                .build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        // Generate the streaming URL
        String streamingUrl = s3Client.utilities().getUrl(GetUrlRequest.builder()
                .bucket(bucketName)
                .key(s3Path)
                .build()).toString();

        log.info("File uploaded successfully. Streaming URL: {}", streamingUrl);

        // Create and return the metadata
        return VideoMetadata.builder()
                .nomFichier(fileName)
                .cheminStockage(s3Path)
                .mimeType(file.getContentType())
                .tailleFichier(file.getSize())
                .urlStreaming(streamingUrl)
                .qualitesDisponibles(List.of(QualiteStream.valueOf(qualite)))
                .dateUpload(LocalDateTime.now())
                .build();
    }

    /**
     * Deletes a file from S3
     * 
     * @param s3Path The path of the file in S3
     */
    public void deleteFile(String s3Path) {
        log.info("Deleting file from S3: {}", s3Path);

        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Path)
                .build());

        log.info("File deleted successfully");
    }

    /**
     * Generates a URL for accessing the file
     * 
     * @param s3Path The path of the file in S3
     * @return The URL for accessing the file
     */
    public String generateUrl(String s3Path) {
        return s3Client.utilities().getUrl(GetUrlRequest.builder()
                .bucket(bucketName)
                .key(s3Path)
                .build()).toString();
    }

    /**
     * Adds a new quality version to an existing VideoMetadata
     * 
     * @param file The file to upload
     * @param existingMetadata The existing VideoMetadata
     * @param qualite The quality of the video (SD_480P, HD_720P, etc.)
     * @return Updated VideoMetadata with the new quality
     * @throws IOException If there's an error reading the file
     */
    public VideoMetadata addQualite(MultipartFile file, VideoMetadata existingMetadata, String qualite) throws IOException {
        // Extract the base filename without the UUID prefix
        String originalFileName = existingMetadata.getNomFichier().substring(existingMetadata.getNomFichier().indexOf("_") + 1);

        // Generate a new unique filename with the same base name
        String fileName = UUID.randomUUID() + "_" + originalFileName;

        // S3 path for the new quality
        String s3Path = "videos/" + qualite + "/" + fileName;

        log.info("Adding new quality {} for file {}, S3 path: {}", qualite, originalFileName, s3Path);

        // Upload the file to S3
        s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Path)
                .contentType(file.getContentType())
                .build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        // Create a new list of qualities by combining the existing ones with the new one
        List<QualiteStream> updatedQualities = new ArrayList<>();
        if (existingMetadata.getQualitesDisponibles() != null) {
            updatedQualities.addAll(existingMetadata.getQualitesDisponibles());
        }

        // Add the new quality if it's not already in the list
        QualiteStream newQuality = QualiteStream.valueOf(qualite);
        if (!updatedQualities.contains(newQuality)) {
            updatedQualities.add(newQuality);
        }

        // Update the existing metadata with the new quality
        existingMetadata.setQualitesDisponibles(updatedQualities);

        log.info("New quality added successfully. Available qualities: {}", updatedQualities);

        return existingMetadata;
    }
}
