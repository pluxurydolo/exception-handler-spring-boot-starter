package com.pluxurydolo.exception.configuration;

import com.pluxurydolo.exception.scheduler.hook.DeleteFilesSchedulerHandlerHook;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

@TestConfiguration
public class SchedulerTestConfiguration {

    @Bean
    public DeleteFilesSchedulerHandlerHook deleteFilesSchedulerHandlerHook() {
        return _ -> Mono.just("");
    }
}
