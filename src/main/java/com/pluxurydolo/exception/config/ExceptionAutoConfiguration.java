package com.pluxurydolo.exception.config;

import com.pluxurydolo.exception.handler.DefaultExceptionHandler;
import com.pluxurydolo.exception.utils.FileCreator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class ExceptionAutoConfiguration {

    @Bean
    public DefaultExceptionHandler defaultExceptionHandler(FileCreator fileCreator) {
        return new DefaultExceptionHandler(fileCreator);
    }

    @Bean
    public FileCreator fileCreator() {
        return new FileCreator();
    }
}
