package com.mtanevski.master.caa.lib.impl.agents;

import com.mtanevski.master.caa.lib.CaaAgent;

public class CaaAgentFactory {

    public static CaaAgent getAgent(CaaAgentType caaAgentType) {
        if (CaaAgentType.ORIGINAL.equals(caaAgentType)) {
            return new OriginalCaaAgent();
        } else if (CaaAgentType.UNTRAVELED_EDGE.equals(caaAgentType)) {
            return new UntraveledEdgeCaaAgent();
        } else {
            throw new IllegalArgumentException("Can not get agent for invalid agent type");
        }
    }

}

