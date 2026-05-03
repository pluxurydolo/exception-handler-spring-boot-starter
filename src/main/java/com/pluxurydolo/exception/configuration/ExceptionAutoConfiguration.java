package com.pluxurydolo.exception.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({
    HandlerConfiguration.class,
    IOConfiguration.class
})
public class ExceptionAutoConfiguration {
}
