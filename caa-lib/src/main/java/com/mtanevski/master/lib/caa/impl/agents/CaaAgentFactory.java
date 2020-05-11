package com.mtanevski.master.lib.caa.impl.agents;

import com.mtanevski.master.lib.caa.CaaAgent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CaaAgentFactory {

    public static List<CaaAgentType> getAvailableAgentTypes() {
        return Arrays.asList(CaaAgentType.values());
    }

    public static List<String> getAvailableAgentTypesAsStrings() {
        return Arrays.stream(CaaAgentType.values()).map(Enum::toString).collect(Collectors.toList());
    }

    public static CaaAgent getAgent(CaaAgentType caaAgentType) {
        if (CaaAgentType.ORIGINAL.equals(caaAgentType)) {
            return new OriginalCaaAgent();
        } else if (CaaAgentType.ADVANCED.equals(caaAgentType)) {
            return new AdvancedCaaAgent();
        } else {
            throw new IllegalArgumentException();
        }
    }

    public enum CaaAgentType {
        ORIGINAL, ADVANCED;
    }
}

