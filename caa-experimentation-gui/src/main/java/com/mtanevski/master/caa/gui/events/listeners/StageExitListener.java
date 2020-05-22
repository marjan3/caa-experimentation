package com.mtanevski.master.caa.gui.events.listeners;

import com.mtanevski.master.caa.gui.events.StageExitEvent;
import javafx.application.Platform;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class StageExitListener implements ApplicationListener<StageExitEvent> {

    private final ConfigurableApplicationContext context;

    public StageExitListener(ConfigurableApplicationContext context) {
        this.context = context;
    }

    @Override
    public void onApplicationEvent(StageExitEvent stageExitEvent) {
        this.context.stop();
        Platform.exit();
    }
}
