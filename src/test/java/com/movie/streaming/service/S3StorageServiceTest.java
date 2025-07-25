package com.movie.streaming.service;

import com.movie.streaming.entity.VideoMetadata;
import com.movie.streaming.enums.QualiteStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for TestS3StorageService which simulates S3 operations without connecting to AWS
 */
public class S3StorageServiceTest {

    private TestS3StorageService testS3StorageService;

    @BeforeEach
    public void setup() {
        // Create the test service
        testS3StorageService = new TestS3StorageService(null);
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

        // Upload the file
        VideoMetadata metadata = testS3StorageService.uploadFile(file, "HD_720P");

        // Verify the metadata
        assertNotNull(metadata);
        assertEquals("video/mp4", metadata.getMimeType());
        assertEquals(content.getBytes().length, metadata.getTailleFichier());
        assertTrue(metadata.getNomFichier().endsWith("_test.mp4"));
        assertTrue(metadata.getCheminStockage().startsWith("videos/HD_720P/"));
        assertTrue(metadata.getUrlStreaming().startsWith("http://test-server/"));
        assertEquals(1, metadata.getQualitesDisponibles().size());
        assertEquals(QualiteStream.HD_720P, metadata.getQualitesDisponibles().get(0));

        // Verify that the file was stored in the in-memory storage
        Map<String, byte[]> fileStorage = testS3StorageService.getFileStorage();
        assertTrue(fileStorage.containsKey(metadata.getCheminStockage()));
        assertArrayEquals(content.getBytes(), fileStorage.get(metadata.getCheminStockage()));
    }

    @Test
    public void testDeleteFile() throws IOException {
        // First upload a file
        String content = "Test file to delete";
        MockMultipartFile file = new MockMultipartFile(
            "file", 
            "test-delete.mp4", 
            "video/mp4", 
            content.getBytes()
        );

        VideoMetadata metadata = testS3StorageService.uploadFile(file, "HD_720P");
        String s3Path = metadata.getCheminStockage();

        // Verify it was uploaded
        Map<String, byte[]> fileStorage = testS3StorageService.getFileStorage();
        assertTrue(fileStorage.containsKey(s3Path));

        // Delete the file
        testS3StorageService.deleteFile(s3Path);

        // Verify it was deleted
        assertFalse(fileStorage.containsKey(s3Path));
    }

    @Test
    public void testGenerateUrl() {
        // Generate a URL
        String s3Path = "videos/HD_720P/test.mp4";
        String url = testS3StorageService.generateUrl(s3Path);

        // Verify the URL
        assertNotNull(url);
        assertEquals("http://test-server/" + s3Path, url);
    }

    @Test
    public void testAddQualite() throws IOException {
        // First upload a file
        String content = "Original content";
        MockMultipartFile file = new MockMultipartFile(
            "file", 
            "original.mp4", 
            "video/mp4", 
            content.getBytes()
        );

        // Upload the original quality
        VideoMetadata existingMetadata = testS3StorageService.uploadFile(file, "HD_720P");

        // Create a test file for the new quality
        String newContent = "4K content";
        MockMultipartFile newFile = new MockMultipartFile(
            "file", 
            "original.mp4", 
            "video/mp4", 
            newContent.getBytes()
        );

        // Add a new quality
        VideoMetadata updatedMetadata = testS3StorageService.addQualite(newFile, existingMetadata, "UHD_2160P");

        // Verify the updated metadata
        assertNotNull(updatedMetadata);
        assertEquals(2, updatedMetadata.getQualitesDisponibles().size());
        assertTrue(updatedMetadata.getQualitesDisponibles().contains(QualiteStream.HD_720P));
        assertTrue(updatedMetadata.getQualitesDisponibles().contains(QualiteStream.UHD_2160P));

        // Verify that both files are in the storage
        Map<String, byte[]> fileStorage = testS3StorageService.getFileStorage();
        assertTrue(fileStorage.containsKey(existingMetadata.getCheminStockage()));

        // Find the new file path (it will be in a different path with UHD_2160P)
        boolean found = false;
        for (String path : fileStorage.keySet()) {
            if (path.contains("UHD_2160P") && path.contains("original.mp4")) {
                found = true;
                assertArrayEquals(newContent.getBytes(), fileStorage.get(path));
                break;
            }
        }
        assertTrue(found, "New quality file not found in storage");
    }
}
