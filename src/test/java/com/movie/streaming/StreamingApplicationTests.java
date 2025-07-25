package com.movie.streaming;

import com.movie.streaming.config.TestConfig;
import com.movie.streaming.config.TestS3Config;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import({TestConfig.class, TestS3Config.class})
class StreamingApplicationTests {

    @Test
    void contextLoads() {
    }

}
