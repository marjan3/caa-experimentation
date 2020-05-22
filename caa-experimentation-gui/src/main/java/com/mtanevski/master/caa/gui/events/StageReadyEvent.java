package com.mtanevski.master.caa.gui.events;

import javafx.stage.Stage;
import org.springframework.context.ApplicationEvent;

public class StageReadyEvent extends ApplicationEvent {
    public StageReadyEvent(Stage source) {
        super(source);
    }

    public Stage getStage() {
        return (Stage) getSource();
    }
}
