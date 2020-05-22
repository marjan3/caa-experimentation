package com.mtanevski.master.caa.lib.impl.agents;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum CaaAgentType {
    ORIGINAL, UNTRAVELED_EDGE;

    public static List<CaaAgentType> listValues() {
        return Arrays.asList(CaaAgentType.values());
    }

    public static List<String> stringValues() {
        return Arrays.stream(CaaAgentType.values()).map(Enum::toString).collect(Collectors.toList());
    }
}
