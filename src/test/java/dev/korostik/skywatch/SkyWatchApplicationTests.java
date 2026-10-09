package dev.korostik.skywatch;

import dev.korostik.skywatch.config.TestContainerConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestContainerConfiguration.class)
class SkyWatchApplicationTests {

    @Test
    void contextLoads() {
    }

}
