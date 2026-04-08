package com.pluxurydolo.exception.util;

import com.pluxurydolo.exception.TestApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static reactor.test.StepVerifier.create;

@SpringBootTest(classes = TestApplication.class)
class FileCreatorIntegrationTests {

    @Autowired
    private FileCreator fileCreator;

    @Test
    void testCreatePermFile() {
        Mono<Path> result = fileCreator.createPermFile("folder", "file.txt", List.of("line"));

        create(result)
            .expectNextMatches(path -> {
                long fileLength = path.toFile().length();

                assertThat(path)
                    .exists();
                assertThat(fileLength)
                    .isIn(5L, 6L);

                return true;
            })
            .verifyComplete();
    }
}
