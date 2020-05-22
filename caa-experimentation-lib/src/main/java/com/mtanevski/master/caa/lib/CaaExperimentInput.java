package com.mtanevski.master.caa.lib;

import com.mtanevski.master.caa.lib.impl.agents.CaaAgentType;

public class CaaExperimentInput {

    private final CaaAgentType agentType;
    private final Double increment;
    private final Double decrement;

    private CaaExperimentInput(CaaAgentType agentType,
                               Double increment,
                               Double decrement) {
        this.agentType = agentType;
        this.increment = increment;
        this.decrement = decrement;
    }

    public static CaaExperimentInput create(CaaAgentType agentType) {
        return create(agentType, Constants.HAPPY_INCREMENTER, Constants.SAD_INCREMENTER);
    }

    public static CaaExperimentInput create(CaaAgentType agentType,
                                            Double increment,
                                            Double decrement) {
        return new CaaExperimentInput(agentType, increment, decrement);
    }

    public CaaAgentType getAgentType() {
        return agentType;
    }

    public Double getIncrement() {
        return increment;
    }

    public Double getDecrement() {
        return decrement;
    }

}
