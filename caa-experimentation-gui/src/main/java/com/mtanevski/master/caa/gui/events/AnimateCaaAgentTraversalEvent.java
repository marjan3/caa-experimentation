package com.mtanevski.master.caa.gui.events;

import com.mtanevski.master.caa.lib.impl.agents.CaaAgentType;
import org.springframework.context.ApplicationEvent;

public class AnimateCaaAgentTraversalEvent extends ApplicationEvent {
    private final Boolean shouldAnimate;

    public AnimateCaaAgentTraversalEvent(CaaAgentType caaAgentType, Boolean shouldAnimate) {
        super(caaAgentType);
        this.shouldAnimate = shouldAnimate;
    }

    public Boolean shouldAnimate() {
        return shouldAnimate;
    }

    public CaaAgentType getCaaAgentType() {
        return (CaaAgentType) source;
    }
}
