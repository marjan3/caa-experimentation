package com.mtanevski.master.caa.lib;

import com.mtanevski.master.caa.lib.impl.agents.CaaAgentType;
import com.mtanevski.master.caa.lib.impl.graphs.tinker.TinkerCaaGraph;
import org.junit.Test;

import static com.mtanevski.master.caa.utils.ConsoleUtils.getExperimentResultsAsStringTableHeaders;
import static com.mtanevski.master.caa.utils.ConsoleUtils.getExperimentResultsAsStringTableRow;
import static com.mtanevski.master.caa.utils.ConsoleUtils.getGraphDetailsString;
import static org.junit.Assert.assertNotNull;

public class CaaImplementationWithMultipleExperimentsPerGraphTest extends CaaImplementationBaseTest {

    public CaaImplementationWithMultipleExperimentsPerGraphTest(String graphFilename, String graphLocation) {
        super(graphFilename, graphLocation);
    }

    @Test
    public void shouldFindHappyPathForGraphWithOriginalAgentForMultipleExperiments() {
        int numberOfExperiments = 10; // = 30;
        final int startIndex = 0; // = 1;
        for (int i = startIndex; i < numberOfExperiments; i++) {
            TinkerCaaGraph graph = new TinkerCaaGraph(super.graphLocation, false);
            CaaExperimentInput experiment = CaaExperimentInput.create(CaaAgentType.ORIGINAL);
            CaaExperimenter caaExperimenter = new CaaExperimenter();

            CaaExperimentResults results = caaExperimenter.executeExperiment(graph, experiment);
            assertNotNull(results);

            // printing to console
            if (i == startIndex) {
                final String graphDetails = getGraphDetailsString(super.graphFilename, graph, results);
                System.out.print(graphDetails);
                String tableHeaders = getExperimentResultsAsStringTableHeaders();
                System.out.print(tableHeaders);
            }
            final String experimentResultsAsString = getExperimentResultsAsStringTableRow(i, results);
            System.out.print(experimentResultsAsString);
        }
    }

}
