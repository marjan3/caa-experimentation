package com.mtanevski.master.caa.gui.events;

import org.springframework.context.ApplicationEvent;

public class SaveResultsEvent extends ApplicationEvent {
    public SaveResultsEvent() {
        super("no-source");
    }
}
