package com.mtanevski.master.caa.gui.events;

import org.springframework.context.ApplicationEvent;

import java.io.File;

public class OpenGraphEvent extends ApplicationEvent {
    public OpenGraphEvent(File file) {
        super(file);
    }

    public File getFile() {
        return (File) source;
    }


}
