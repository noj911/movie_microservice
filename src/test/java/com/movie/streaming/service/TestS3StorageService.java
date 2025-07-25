package com.movie.streaming.service;

import com.movie.streaming.entity.VideoMetadata;
import com.movie.streaming.enums.QualiteStream;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Test implementation of S3 storage service that simulates S3 operations without connecting to AWS
 */
@Service
@Primary
@Profile("test")
public class TestS3StorageService extends S3StorageService {

    // In-memory storage for uploaded files
    private final Map<String, byte[]> fileStorage = new HashMap<>();

    /**
     * Constructor that sets up a mock S3Client
     */
    @Autowired
    public TestS3StorageService(S3Client s3Client) {
        super(s3Client);
        ReflectionTestUtils.setField(this, "bucketName", "test-bucket");
    }

    /**
     * Get access to the in-memory file storage for verification in tests
     * 
     * @return Map of S3 paths to file contents
     */
    public Map<String, byte[]> getFileStorage() {
        return fileStorage;
    }

    /**
     * Overrides the uploadFile method to store files in memory instead of S3
     */
    @Override
    public VideoMetadata uploadFile(MultipartFile file, String qualite) throws IOException {
        // Generate a unique filename
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // S3 path (e.g., videos/HD_720P/myfile.mp4)
        String s3Path = "videos/" + qualite + "/" + fileName;

        // Store the file content in memory
        fileStorage.put(s3Path, file.getBytes());

        // Simulate the streaming URL
        String streamingUrl = "http://test-server/" + s3Path;

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
     * Overrides the deleteFile method to remove files from memory instead of S3
     */
    @Override
    public void deleteFile(String s3Path) {
        fileStorage.remove(s3Path);
    }

    /**
     * Overrides the generateUrl method to return a test URL instead of an S3 URL
     */
    @Override
    public String generateUrl(String s3Path) {
        return "http://test-server/" + s3Path;
    }

    /**
     * Overrides the addQualite method to store new quality versions in memory instead of S3
     */
    @Override
    public VideoMetadata addQualite(MultipartFile file, VideoMetadata existingMetadata, String qualite) throws IOException {
        // Extract the base filename without the UUID prefix
        String originalFileName = existingMetadata.getNomFichier().substring(existingMetadata.getNomFichier().indexOf("_") + 1);

        // Generate a new unique filename with the same base name
        String fileName = UUID.randomUUID() + "_" + originalFileName;

        // S3 path for the new quality
        String s3Path = "videos/" + qualite + "/" + fileName;

        // Store the file content in memory
        fileStorage.put(s3Path, file.getBytes());

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

        return existingMetadata;
    }
}
