package com.pluxurydolo.exception.io;

import com.pluxurydolo.exception.exception.CreateDirectoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DirectoryCreator {
    private static final Logger LOGGER = LoggerFactory.getLogger(DirectoryCreator.class);

    public Mono<Path> create(Path directoryPath) {
        return Mono.fromCallable(() -> createDirectoryIfEmpty(directoryPath))
            .doOnSuccess(_ -> LOGGER.info("deam [exception-starter] Успешно создана директория {}", directoryPath))
            .onErrorResume(throwable -> {
                LOGGER.error("ktfi [exception-starter] Ошибка при создании директории {}", directoryPath);
                return Mono.error(new CreateDirectoryException(throwable));
            })
            .subscribeOn(Schedulers.boundedElastic());
    }

    private static Path createDirectoryIfEmpty(Path directoryPath) throws IOException {
        if (Files.notExists(directoryPath)) {
            return Files.createDirectories(directoryPath);
        }
        return directoryPath;
    }
}
