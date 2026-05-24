package com.pluxurydolo.exception.scheduler;

import com.pluxurydolo.exception.scheduler.handler.DeleteFilesSchedulerHandler;
import org.springframework.scheduling.annotation.Scheduled;

public class DeleteFilesScheduler {
    private final DeleteFilesSchedulerHandler handler;

    public DeleteFilesScheduler(DeleteFilesSchedulerHandler handler) {
        this.handler = handler;
    }

    @Scheduled(
        cron = "${exception.scheduler.delete-files.cron}",
        zone = "${exception.scheduler.delete-files.zone}"
    )
    public void schedule() {
        String jobName = getClass().getName();

        handler.handle(jobName)
            .subscribe();
    }
}
