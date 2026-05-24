package com.pluxurydolo.exception.scheduler.handler;

import com.pluxurydolo.exception.exception.CleanLogsDirectoryException;
import com.pluxurydolo.exception.exception.DeleteDirectoryException;
import com.pluxurydolo.exception.exception.DeleteLogsDirectoriesException;
import com.pluxurydolo.exception.exception.DeletePathException;
import com.pluxurydolo.exception.scheduler.hook.DeleteFilesSchedulerHandlerHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import static java.time.LocalDate.now;
import static java.util.Comparator.reverseOrder;

public class DeleteFilesSchedulerHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteFilesSchedulerHandler.class);

    private final DeleteFilesSchedulerHandlerHook deleteFilesSchedulerHandlerHook;

    public DeleteFilesSchedulerHandler(DeleteFilesSchedulerHandlerHook deleteFilesSchedulerHandlerHook) {
        this.deleteFilesSchedulerHandlerHook = deleteFilesSchedulerHandlerHook;
    }

    public Mono<String> handle(String jobName) {
        LOGGER.info("qgdu [exception-handler-starter] Стартовала джоба {}", jobName);

        Path logsDirectory = Paths.get("logs");

        return Mono.fromCallable(() -> deleteExpiredLogs(logsDirectory))
            .flatMap(deleteFilesSchedulerHandlerHook::doAfter)
            .thenReturn(jobName)
            .doOnSuccess(_ -> LOGGER.info("ocnh [exception-handler-starter] Успешно очищена директория с устаревшими логами {}", logsDirectory))
            .onErrorResume(throwable -> {
                LOGGER.error("wgah [exception-handler-starter] Произошла ошибка при очистке директории с устаревшими логами {}", logsDirectory, throwable);
                return Mono.error(new CleanLogsDirectoryException(throwable));
            })
            .subscribeOn(Schedulers.boundedElastic());
    }

    private static int deleteExpiredLogs(Path logsDirectory) {
        try (Stream<Path> paths = Files.list(logsDirectory)) {
            return paths.filter(Files::isDirectory)
                .filter(DeleteFilesSchedulerHandler::isExpired)
                .map(DeleteFilesSchedulerHandler::deleteDirectory)
                .mapToInt(Integer::intValue)
                .sum();
        } catch (IOException exception) {
            throw new DeleteLogsDirectoriesException(exception);
        }
    }

    private static boolean isExpired(Path directory) {
        LocalDate expirationDate = now().minusWeeks(1);

        String folderName = directory.getFileName().toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate folderDate = LocalDate.parse(folderName, formatter);

        return folderDate.isBefore(expirationDate);
    }

    private static int deleteDirectory(Path directory) {
        try (Stream<Path> walk = Files.walk(directory)) {
            List<Path> filesInside = walk.sorted(reverseOrder())
                .toList();

            filesInside.forEach(DeleteFilesSchedulerHandler::deletePath);

            return filesInside.size() - 1;
        } catch (IOException exception) {
            throw new DeleteDirectoryException(exception);
        }
    }

    private static void deletePath(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException exception) {
            throw new DeletePathException(exception);
        }
    }
}
