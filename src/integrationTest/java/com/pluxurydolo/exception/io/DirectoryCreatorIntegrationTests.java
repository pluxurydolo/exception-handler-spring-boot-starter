package com.pluxurydolo.exception.io;

import com.pluxurydolo.exception.base.AbstractIntegrationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static reactor.test.StepVerifier.create;

class DirectoryCreatorIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    private DirectoryCreator directoryCreator;

    @Test
    void testCreateDirectory() {
        Mono<Path> result = directoryCreator.create(Paths.get("directory/"));

        create(result)
            .expectNextMatches(path -> {
                boolean canWrite = path.toFile().canWrite();

                assertThat(path)
                    .exists()
                    .isDirectory();

                assertThat(canWrite)
                    .isTrue();

                return true;
            })
            .verifyComplete();
    }
}
