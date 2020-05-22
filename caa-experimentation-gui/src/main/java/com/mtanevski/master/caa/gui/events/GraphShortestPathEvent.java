package com.mtanevski.master.caa.gui.events;

import org.springframework.context.ApplicationEvent;

public class GraphShortestPathEvent extends ApplicationEvent {
    public GraphShortestPathEvent(boolean selected) {
        super(selected);
    }

    public boolean shouldMark() {
        return (Boolean) source;
    }
}
