package com.movie.streaming.controller;

import com.movie.streaming.entity.VideoMetadata;
import com.movie.streaming.enums.QualiteStream;
import com.movie.streaming.service.S3StorageService;
import com.movie.streaming.service.SerieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests for StorageController using mocked S3StorageService
 */
@ExtendWith(MockitoExtension.class)
public class StorageControllerTest {

    @Mock
    private S3StorageService s3StorageService;

    @Mock
    private SerieService serieService;

    @InjectMocks
    private StorageController storageController;

    // In-memory storage for simulating file storage
    private Map<String, byte[]> fileStorage = new HashMap<>();

    @BeforeEach
    public void setup() {
        // Clear the file storage before each test
        fileStorage.clear();
    }

    @Test
    public void testUploadFile() throws IOException {
        // Create a test file
        String content = "Test file content";
        MockMultipartFile file = new MockMultipartFile(
            "file", 
            "test.mp4", 
            "video/mp4", 
            content.getBytes()
        );

        // Mock the S3StorageService response
        VideoMetadata mockMetadata = VideoMetadata.builder()
            .nomFichier(UUID.randomUUID() + "_test.mp4")
            .cheminStockage("videos/HD_720P/test.mp4")
            .mimeType("video/mp4")
            .tailleFichier(content.getBytes().length)
            .urlStreaming("http://test-server/videos/HD_720P/test.mp4")
            .qualitesDisponibles(List.of(QualiteStream.HD_720P))
            .dateUpload(LocalDateTime.now())
            .build();

        when(s3StorageService.uploadFile(any(MockMultipartFile.class), eq("HD_720P")))
            .thenReturn(mockMetadata);

        // Upload the file
        ResponseEntity<VideoMetadata> response = storageController.uploadFile(file, "HD_720P");

        // Verify the response
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        // Verify that the S3StorageService was called
        verify(s3StorageService).uploadFile(file, "HD_720P");

        // Verify the metadata
        VideoMetadata metadata = response.getBody();
        assertEquals("video/mp4", metadata.getMimeType());
        assertEquals(content.getBytes().length, metadata.getTailleFichier());
        assertTrue(metadata.getUrlStreaming().startsWith("http://test-server/"));
        assertEquals(1, metadata.getQualitesDisponibles().size());
        assertEquals(QualiteStream.HD_720P, metadata.getQualitesDisponibles().get(0));
    }

    @Test
    public void testUploadEpisodeContent() throws IOException {
        // Create a test file
        String content = "Episode content";
        MockMultipartFile file = new MockMultipartFile(
            "file", 
            "episode1.mp4", 
            "video/mp4", 
            content.getBytes()
        );

        // Mock the S3StorageService response
        VideoMetadata mockMetadata = VideoMetadata.builder()
            .nomFichier(UUID.randomUUID() + "_episode1.mp4")
            .cheminStockage("videos/HD_720P/episode1.mp4")
            .mimeType("video/mp4")
            .tailleFichier(content.getBytes().length)
            .urlStreaming("http://test-server/videos/HD_720P/episode1.mp4")
            .qualitesDisponibles(List.of(QualiteStream.HD_720P))
            .dateUpload(LocalDateTime.now())
            .build();

        when(s3StorageService.uploadFile(any(MockMultipartFile.class), eq("HD_720P")))
            .thenReturn(mockMetadata);

        // Upload the file for an episode
        ResponseEntity<?> response = storageController.uploadEpisodeContent(
            file, "serie123", 1, 1, "HD_720P"
        );

        // Verify the response
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        // Verify that the S3StorageService was called
        verify(s3StorageService).uploadFile(file, "HD_720P");

        // Verify that the metadata was associated with the episode
        verify(serieService).associerVideoMetadataAEpisode(eq("serie123"), eq(1), eq(1), any(VideoMetadata.class));
    }

    @Test
    public void testDeleteFile() throws IOException {
        // Define a test S3 path
        String s3Path = "videos/HD_720P/test-delete.mp4";

        // Mock the delete operation
        doNothing().when(s3StorageService).deleteFile(s3Path);

        // Call the delete method
        ResponseEntity<Void> deleteResponse = storageController.deleteFile(s3Path);

        // Verify the response
        assertEquals(200, deleteResponse.getStatusCodeValue());

        // Verify that the S3StorageService was called to delete the file
        verify(s3StorageService).deleteFile(s3Path);
    }

    @Test
    public void testGenerateUrl() {
        // Test URL generation
        String s3Path = "videos/HD_720P/test-url.mp4";
        String expectedUrl = "http://test-server/" + s3Path;

        // Mock the generateUrl method
        when(s3StorageService.generateUrl(s3Path)).thenReturn(expectedUrl);

        // Call the controller method
        ResponseEntity<String> response = storageController.getUrl(s3Path);

        // Verify the response
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedUrl, response.getBody());

        // Verify that the S3StorageService was called
        verify(s3StorageService).generateUrl(s3Path);
    }

    @Test
    public void testAddEpisodeQuality() throws IOException {
        // Mock the existing metadata
        VideoMetadata existingMetadata = VideoMetadata.builder()
            .nomFichier("uuid_original.mp4")
            .cheminStockage("videos/HD_720P/uuid_original.mp4")
            .qualitesDisponibles(List.of(QualiteStream.HD_720P))
            .build();

        // Create updated metadata with both qualities
        VideoMetadata updatedMetadata = VideoMetadata.builder()
            .nomFichier("uuid_original.mp4")
            .cheminStockage("videos/HD_720P/uuid_original.mp4")
            .qualitesDisponibles(List.of(QualiteStream.HD_720P, QualiteStream.UHD_2160P))
            .build();

        // Mock the service to return the existing metadata
        when(serieService.getEpisodeVideoMetadata("serie123", 1, 1))
            .thenReturn(existingMetadata);

        // Mock the S3StorageService.addQualite method
        when(s3StorageService.addQualite(any(MultipartFile.class), eq(existingMetadata), eq("UHD_2160P")))
            .thenReturn(updatedMetadata);

        // Create a test file for the new quality
        String content = "4K content";
        MockMultipartFile file = new MockMultipartFile(
            "file", 
            "original.mp4", 
            "video/mp4", 
            content.getBytes()
        );

        // Add a new quality
        ResponseEntity<?> response = storageController.addEpisodeQuality(
            file, "serie123", 1, 1, "UHD_2160P"
        );

        // Verify the response
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        // Verify that the S3StorageService was called to add the quality
        verify(s3StorageService).addQualite(eq(file), eq(existingMetadata), eq("UHD_2160P"));

        // Verify that the updated metadata was saved
        verify(serieService).updateEpisodeVideoMetadata(
            eq("serie123"), eq(1), eq(1), eq(updatedMetadata)
        );

        // Verify that the new quality was added to the metadata
        VideoMetadata responseMetadata = (VideoMetadata) response.getBody();
        assertTrue(responseMetadata.getQualitesDisponibles().contains(QualiteStream.UHD_2160P));
        assertTrue(responseMetadata.getQualitesDisponibles().contains(QualiteStream.HD_720P));
    }
}
