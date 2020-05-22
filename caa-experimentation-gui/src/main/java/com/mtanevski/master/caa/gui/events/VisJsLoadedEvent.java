package com.mtanevski.master.caa.gui.events;

import org.springframework.context.ApplicationEvent;

public class VisJsLoadedEvent extends ApplicationEvent {
    public VisJsLoadedEvent() {
        super("no-source");
    }
}
