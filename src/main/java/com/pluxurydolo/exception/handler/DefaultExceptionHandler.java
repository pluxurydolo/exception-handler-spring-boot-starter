package com.pluxurydolo.exception.handler;

import com.pluxurydolo.exception.utils.FileCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.time.ZonedDateTime.now;

public class DefaultExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    private final FileCreator fileCreator;

    public DefaultExceptionHandler(FileCreator fileCreator) {
        this.fileCreator = fileCreator;
    }

    public <T> Mono<T> handleAndRethrow(Throwable throwable) {
        return handle(throwable)
            .then(Mono.error(throwable));
    }

    public Mono<String> handle(Throwable throwable) {
        String simpleErrorName = throwable.getClass().getSimpleName();
        LOGGER.error("ekik Возникло исключение {}", simpleErrorName);

        String folder = "logs";
        String fileName = buildFileName(simpleErrorName);
        String stackTrace = retrieveStackTrace(throwable);

        return fileCreator.createPermFile(folder, fileName, List.of(stackTrace))
            .thenReturn(simpleErrorName);
    }

    private static String buildFileName(String simpleErrorName) {
        String pattern = "MM-dd-yyyy_HH-mm-ss_z";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        String now = now().format(formatter);

        return now + "___" + simpleErrorName + ".txt";
    }

    private static String retrieveStackTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter, true);
        throwable.printStackTrace(printWriter);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }
}
