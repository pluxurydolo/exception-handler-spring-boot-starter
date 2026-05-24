package com.pluxurydolo.exception.io;

import com.pluxurydolo.exception.base.AbstractIntegrationTests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static reactor.test.StepVerifier.create;

class FileCreatorIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    private FileCreator fileCreator;

    @AfterEach
    void cleanUp() throws IOException {
        Path logsPath = Paths.get("folder");

        if (Files.exists(logsPath)) {
            Files.walk(logsPath)
                .sorted((a, b) -> -a.compareTo(b))
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException exception) {
                        throw new IllegalStateException(exception);
                    }
                });
        }
    }

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
