package com.pluxurydolo.exception.configuration;

import com.pluxurydolo.exception.handler.DefaultExceptionHandler;
import com.pluxurydolo.exception.util.FileCreator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class ExceptionAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DefaultExceptionHandler defaultExceptionHandler(FileCreator fileCreator) {
        return new DefaultExceptionHandler(fileCreator);
    }

    @Bean
    @ConditionalOnMissingBean
    public FileCreator fileCreator() {
        return new FileCreator();
    }
}
