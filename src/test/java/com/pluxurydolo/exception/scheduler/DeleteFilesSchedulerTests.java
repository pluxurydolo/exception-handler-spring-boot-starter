package com.pluxurydolo.exception.scheduler;

import com.pluxurydolo.exception.scheduler.handler.DeleteFilesSchedulerHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteFilesSchedulerTests {

    @Mock
    private DeleteFilesSchedulerHandler deleteFilesSchedulerHandler;

    @InjectMocks
    private DeleteFilesScheduler scheduler;

    @Test
    void testSchedule() {
        when(deleteFilesSchedulerHandler.handle(anyString()))
            .thenReturn(Mono.just(""));

        assertDoesNotThrow(scheduler::schedule);
    }
}
