package com.pluxurydolo.exception.handler;

import com.pluxurydolo.exception.base.AbstractIntegrationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static reactor.test.StepVerifier.create;

class DefaultExceptionHandlerIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    private DefaultExceptionHandler defaultExceptionHandler;

    @Test
    void testHandleAndRethrow() {
        Mono<Object> result = defaultExceptionHandler.handleAndRethrow(new RuntimeException());

        create(result)
            .expectErrorMatches(throwable -> {
                Path folderPath = Paths.get("./logs");

                assertThat(folderPath)
                    .isDirectoryContaining("glob:**___RuntimeException.txt");
                assertThat(throwable)
                    .isInstanceOf(RuntimeException.class);

                return true;
            })
            .verify();
    }

    @Test
    void testHandle() {
        Mono<String> result = defaultExceptionHandler.handle(new RuntimeException());

        create(result)
            .expectNextMatches(_ -> {
                Path folderPath = Paths.get("./logs");

                assertThat(folderPath)
                    .isDirectoryContaining("glob:**___RuntimeException.txt");

                return true;
            })
            .verifyComplete();
    }
}
