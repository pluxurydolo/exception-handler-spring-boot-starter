package com.pluxurydolo.exception.configuration;

import com.pluxurydolo.exception.scheduler.DeleteFilesScheduler;
import com.pluxurydolo.exception.scheduler.handler.DeleteFilesSchedulerHandler;
import com.pluxurydolo.exception.scheduler.hook.DeleteFilesSchedulerHandlerHook;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class ExceptionSchedulingConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DeleteFilesScheduler deleteExceptionFilesScheduler(
        DeleteFilesSchedulerHandler deleteFilesSchedulerHandler
    ) {
        return new DeleteFilesScheduler(deleteFilesSchedulerHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    public DeleteFilesSchedulerHandler deleteExceptionFilesSchedulerHandler(DeleteFilesSchedulerHandlerHook hook) {
        return new DeleteFilesSchedulerHandler(hook);
    }
}
