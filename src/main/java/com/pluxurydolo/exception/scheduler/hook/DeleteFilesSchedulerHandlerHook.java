package com.pluxurydolo.exception.scheduler.hook;

import reactor.core.publisher.Mono;

public interface DeleteFilesSchedulerHandlerHook {
    Mono<String> doAfter(int deletedFilesCount);
}
