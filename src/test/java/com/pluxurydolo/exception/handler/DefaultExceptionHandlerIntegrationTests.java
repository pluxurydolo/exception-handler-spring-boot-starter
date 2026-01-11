package com.pluxurydolo.exception.handler;

import com.pluxurydolo.exception.application.TestApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static reactor.test.StepVerifier.create;

@SpringBootTest(classes = TestApplication.class)
class DefaultExceptionHandlerIntegrationTests {

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
            .expectNextMatches(fileName -> {
                Path folderPath = Paths.get("./logs");

                assertThat(folderPath)
                    .isDirectoryContaining("glob:**___RuntimeException.txt");

                return true;
            })
            .verifyComplete();
    }
}
