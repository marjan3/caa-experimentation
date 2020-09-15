package com.mtanevski.master.caa.lib;

import com.mtanevski.master.caa.lib.impl.agents.CaaAgentType;
import com.mtanevski.master.caa.lib.impl.graphs.tinker.TinkerCaaGraph;
import org.junit.Test;

import static com.mtanevski.master.caa.utils.ConsoleUtils.printResultsToConsole;

public class CaaImplementationWithOriginalAgentTest extends CaaImplementationBaseTest {

    public CaaImplementationWithOriginalAgentTest(String graphFilename, String graphLocation) {
        super(graphFilename, graphLocation);
    }

    @Test
    public void shouldFindHappyPathForGraphWithOriginalAgent() {
        TinkerCaaGraph graph = new TinkerCaaGraph(super.graphLocation, false);
        CaaExperimentInput experiment = CaaExperimentInput.create(CaaAgentType.ORIGINAL);
        CaaExperimenter caaExperimenter = new CaaExperimenter();

        CaaExperimentResults results = caaExperimenter.executeExperiment(graph, experiment);

        printResultsToConsole(graph, CaaAgentType.ORIGINAL, super.graphFilename, results);
    }
}
