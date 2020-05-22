package com.mtanevski.master.caa.gui.events;

import com.mtanevski.master.caa.lib.impl.agents.CaaAgentType;
import org.springframework.context.ApplicationEvent;

public class AnimateCaaExperimentEvent extends ApplicationEvent {

    private final Double happyMultiplier;
    private final Double sadMultiplier;
    private final CaaAgentType agent;
    private final Boolean shouldAnimate;

    public AnimateCaaExperimentEvent(Double happyMultiplier, Double sadMultiplier, CaaAgentType agent, Boolean shouldAnimate) {
        super("no-source");
        this.happyMultiplier = happyMultiplier;
        this.sadMultiplier = sadMultiplier;
        this.agent = agent;
        this.shouldAnimate = shouldAnimate;
    }

    public Double getHappyMultiplier() {
        return happyMultiplier;
    }

    public Double getSadMultiplier() {
        return sadMultiplier;
    }

    public CaaAgentType getAgent() {
        return agent;
    }

    public Boolean getShouldAnimate() {
        return shouldAnimate;
    }
}
