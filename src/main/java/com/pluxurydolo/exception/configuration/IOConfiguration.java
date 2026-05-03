package com.pluxurydolo.exception.configuration;

import com.pluxurydolo.exception.io.DirectoryCreator;
import com.pluxurydolo.exception.io.FileCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IOConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public FileCreator fileCreator(DirectoryCreator directoryCreator) {
        return new FileCreator(directoryCreator);
    }

    @Bean
    @ConditionalOnMissingBean
    public DirectoryCreator directoryCreator() {
        return new DirectoryCreator();
    }
}
