package com.pluxurydolo.exception.io;

import com.pluxurydolo.exception.exception.CreateFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.Files.write;

public class FileCreator {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileCreator.class);

    private final DirectoryCreator directoryCreator;

    public FileCreator(DirectoryCreator directoryCreator) {
        this.directoryCreator = directoryCreator;
    }

    public Mono<Path> createPermFile(String folder, String filePath, List<String> fileLines) {
        Path fullPath = Paths.get(folder, filePath);
        Path directoryPath = fullPath.getParent();

        return directoryCreator.create(directoryPath)
            .map(_ -> addLines(fullPath, fileLines))
            .doOnSuccess(_ -> LOGGER.info("vrcs [exception-starter] Успешно создан постоянный файл {}", fullPath))
            .onErrorResume(throwable -> {
                LOGGER.error("gisy [exception-starter] Произошла ошибка при создании постоянного файла", throwable);
                return Mono.error(new CreateFileException(throwable));
            });
    }

    private static Path addLines(Path path, List<String> lines) {
        try {
            return write(path, lines);
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
