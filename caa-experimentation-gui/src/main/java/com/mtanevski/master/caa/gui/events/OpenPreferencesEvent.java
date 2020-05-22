package com.mtanevski.master.caa.gui.events;

import org.springframework.context.ApplicationEvent;

public class OpenPreferencesEvent extends ApplicationEvent {
    public OpenPreferencesEvent() {
        super("no-source");
    }
}
