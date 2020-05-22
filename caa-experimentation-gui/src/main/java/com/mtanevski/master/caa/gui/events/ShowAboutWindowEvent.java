package com.mtanevski.master.caa.gui.events;

import org.springframework.context.ApplicationEvent;

public class ShowAboutWindowEvent extends ApplicationEvent {
    public ShowAboutWindowEvent() {
        super("no-source");
    }
}
