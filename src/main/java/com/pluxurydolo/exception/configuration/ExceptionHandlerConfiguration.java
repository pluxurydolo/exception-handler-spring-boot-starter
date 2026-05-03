package com.pluxurydolo.exception.configuration;

import com.pluxurydolo.exception.handler.DefaultExceptionHandler;
import com.pluxurydolo.exception.io.FileCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExceptionHandlerConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DefaultExceptionHandler defaultExceptionHandler(FileCreator fileCreator) {
        return new DefaultExceptionHandler(fileCreator);
    }
}
