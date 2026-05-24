package com.pluxurydolo.exception.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({
    ExceptionHandlerConfiguration.class,
    ExceptionIOConfiguration.class,
    ExceptionSchedulingConfiguration.class
})
public class ExceptionAutoConfiguration {
}
