package com.mtanevski.master.lib.caa;

import com.mtanevski.master.lib.caa.impl.agents.AdvancedCaaAgent;
import com.mtanevski.master.lib.caa.impl.graph.tinker.TinkerCaaEdge;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class CaaAgentTest {

    AdvancedCaaAgent caaAgent = new AdvancedCaaAgent();

    @Test
    public void test1() {
        List<CaaEdge> adjacentEdges = Arrays.asList(new TinkerCaaEdge("K", "M", -0.5, 0.0), new TinkerCaaEdge("K", "I", 0.0, 0.0));
        Optional<CaaEdge> caaEdge = caaAgent.pickEdge(adjacentEdges);
        assertEquals("I", caaEdge.get().getTo());
    }

    @Test
    public void test2() {
        Optional<CaaEdge> caaEdge = caaAgent.pickEdge(Arrays.asList(new TinkerCaaEdge("K", "M", -0.5, 0.0), new TinkerCaaEdge("K", "I", 0.5, 0.0)));
        assertEquals("I", caaEdge.get().getTo());
    }
}