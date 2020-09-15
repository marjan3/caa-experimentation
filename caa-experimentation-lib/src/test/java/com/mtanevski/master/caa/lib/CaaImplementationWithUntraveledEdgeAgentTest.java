package com.mtanevski.master.caa.lib;

import com.mtanevski.master.caa.lib.impl.agents.CaaAgentType;
import com.mtanevski.master.caa.lib.impl.graphs.tinker.TinkerCaaGraph;
import org.junit.Test;

import static com.mtanevski.master.caa.utils.ConsoleUtils.printResultsToConsole;

public class CaaImplementationWithUntraveledEdgeAgentTest extends CaaImplementationBaseTest {

    public CaaImplementationWithUntraveledEdgeAgentTest(String graphFilename, String graphLocation) {
        super(graphFilename, graphLocation);
    }

    @Test
    public void shouldFindHappyPathForGraphWithUntraveledEdgeAgent() {
        TinkerCaaGraph graph = new TinkerCaaGraph(super.graphLocation, false);
        CaaExperimentInput experiment = CaaExperimentInput.create(CaaAgentType.UNTRAVELED_EDGE);
        CaaExperimenter caaExperimenter = new CaaExperimenter();

        CaaExperimentResults results = caaExperimenter.executeExperiment(graph, experiment);

        printResultsToConsole(graph, CaaAgentType.UNTRAVELED_EDGE, super.graphFilename, results);
    }
}
