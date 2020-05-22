package com.mtanevski.master.caa.gui.events;

import org.springframework.context.ApplicationEvent;

import java.io.File;

public class OpenResultsEvent extends ApplicationEvent {
    public OpenResultsEvent(File file) {
        super(file);
    }

    public File getFile() {
        return (File) source;
    }
}
