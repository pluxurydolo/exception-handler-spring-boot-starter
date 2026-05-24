package com.pluxurydolo.exception.scheduler;

import com.pluxurydolo.exception.base.AbstractIntegrationTests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class DeleteFilesSchedulerIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    private DeleteFilesScheduler scheduler;

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
    void testSchedule() throws IOException {
        Path oldFolder = createFolder(now().minusDays(15));
        Path oldFile1 = createFile(oldFolder, "old1.txt");
        Path oldFile2 = createFile(oldFolder, "old2.txt");
        Path todayFolder = createFolder(now());
        Path todayFile = createFile(todayFolder, "today.txt");

        assertDoesNotThrow(scheduler::schedule);

        await().atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> {
                assertThat(Files.exists(oldFolder))
                    .isFalse();
                assertThat(Files.exists(oldFile1))
                    .isFalse();
                assertThat(Files.exists(oldFile2))
                    .isFalse();

                assertThat(Files.exists(todayFolder))
                    .isTrue();
                assertThat(Files.exists(todayFile))
                    .isTrue();
            });
    }

    private static Path createFolder(ChronoLocalDate localDate) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String folderName = localDate.format(formatter);

        Path logsPath = Paths.get("logs");
        Path folder = logsPath.resolve(folderName);

        Files.createDirectories(folder);
        return folder;
    }

    private static Path createFile(Path folder, String fileName) throws IOException {
        Path file = folder.resolve(fileName);

        Files.createFile(file);
        return file;
    }
}
