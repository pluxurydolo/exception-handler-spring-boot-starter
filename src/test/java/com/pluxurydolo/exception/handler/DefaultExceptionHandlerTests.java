package com.pluxurydolo.exception.handler;

import com.pluxurydolo.exception.utils.FileCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.nio.file.Path;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class DefaultExceptionHandlerTests {

    @Mock
    private FileCreator fileCreator;

    @InjectMocks
    private DefaultExceptionHandler defaultExceptionHandler;

    @Test
    void testHandleAndRethrow() {
        when(fileCreator.createPermFile(anyString(), anyString(), anyList()))
            .thenReturn(Mono.just(path()));

        Mono<String> result = defaultExceptionHandler.handleAndRethrow(new RuntimeException());

        create(result)
            .expectError(RuntimeException.class)
            .verify();
    }

    @Test
    void testHandle() {
        when(fileCreator.createPermFile(anyString(), anyString(), anyList()))
            .thenReturn(Mono.just(path()));

        Mono<String> result = defaultExceptionHandler.handle(new RuntimeException());

        create(result)
            .expectNext("RuntimeException")
            .verifyComplete();
    }

    private static Path path() {
        return Path.of("");
    }
}
