package com.pluxurydolo.exception.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.write;

public class FileCreator {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileCreator.class);

    public Mono<Path> createPermFile(String folder, String filePath, List<String> fileLines) {
        Path fullPath = Paths.get(folder, filePath);
        Path directoryPath = fullPath.getParent();

        return createDirectoryIfEmpty(directoryPath)
            .map(it -> addLines(fullPath, fileLines))
            .doOnSuccess(it -> LOGGER.info("vrcs Успешно создан постоянный файл {}", fullPath))
            .doOnError(throwable -> LOGGER.error("gisy Произошла ошибка при создании постоянного файла", throwable));
    }

    private static Mono<Path> createDirectoryIfEmpty(Path directoryPath) {
        if (!exists(directoryPath)) {
            return Mono.fromCallable(() -> createDirectory(directoryPath))
                .subscribeOn(Schedulers.boundedElastic());
        }

        return Mono.just(directoryPath);
    }

    private static Path createDirectory(Path path) {
        try {
            return createDirectories(path);
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private static Path addLines(Path path, List<String> lines) {
        try {
            return write(path, lines);
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
