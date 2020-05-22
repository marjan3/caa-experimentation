package com.mtanevski.master.caa.gui.events;

import org.springframework.context.ApplicationEvent;

public class StageExitEvent extends ApplicationEvent {
    public StageExitEvent() {
        super("no-source");
    }
}
