package com.pluxurydolo.exception.handler;

import com.pluxurydolo.exception.base.AbstractIntegrationTests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static reactor.test.StepVerifier.create;

class DefaultExceptionHandlerIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    private DefaultExceptionHandler defaultExceptionHandler;

    @AfterEach
    void cleanUp() throws IOException {
        Path logsPath = Paths.get("logs");

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
    void testHandleAndRethrow() {
        Path logsPath = Paths.get("logs");
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Path todayFolder = logsPath.resolve(today);

        Mono<String> result = defaultExceptionHandler.handleAndRethrow(new RuntimeException());

        create(result)
            .expectErrorMatches(throwable -> {
                assertThat(todayFolder)
                    .isDirectoryContaining("glob:**___RuntimeException.txt");
                assertThat(throwable)
                    .isInstanceOf(RuntimeException.class);

                return true;
            })
            .verify();
    }

    @Test
    void testHandle() {
        Path logsPath = Paths.get("logs");
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Path todayFolder = logsPath.resolve(today);

        Mono<String> result = defaultExceptionHandler.handle(new RuntimeException());

        create(result)
            .expectNextMatches(_ -> {
                assertThat(todayFolder)
                    .isDirectoryContaining("glob:**___RuntimeException.txt");

                return true;
            })
            .verifyComplete();
    }
}
