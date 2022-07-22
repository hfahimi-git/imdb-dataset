package com.hfahimi.lb;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class RunAfterStartup {
    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() throws ExecutionException, InterruptedException {
        Data.build();
    }
}