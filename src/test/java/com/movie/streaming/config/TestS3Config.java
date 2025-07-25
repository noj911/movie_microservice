package com.movie.streaming.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Test configuration that provides a mock S3Client for testing
 */
@Configuration
@Profile("test")
public class TestS3Config {
    
    /**
     * Creates a mock S3Client for testing
     * 
     * @return A mock S3Client
     */
    @Bean
    @Primary
    public S3Client s3Client() {
        return Mockito.mock(S3Client.class);
    }
}