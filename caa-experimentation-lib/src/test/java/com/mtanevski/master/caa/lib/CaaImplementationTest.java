package com.mtanevski.master.caa.lib;

import com.mtanevski.master.caa.lib.impl.agents.CaaAgentType;
import com.mtanevski.master.caa.lib.impl.graphs.tinker.TinkerCaaGraph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RunWith(Parameterized.class)
public class CaaImplementationTest {

    private static final String GRAPHS_DIR = "src/test/resources/graphs/";
    private static final String GRAPH_ML_EXTENSION = ".graphml";

    private final String graphFilename;
    private final String graphLocation;

    public CaaImplementationTest(String graphFilename, String graphLocation) {
        this.graphFilename = graphFilename;
        this.graphLocation = graphLocation;
    }

    @Parameterized.Parameters(name = "{0}")
    public static List<String[]> graphsToTest() {
        try {
            Path graphDirectory = Paths.get(GRAPHS_DIR);
            Predicate<Path> onlyGraphMlFiles = path -> path.getFileName().toString().endsWith(GRAPH_ML_EXTENSION);
            Function<Path, String[]> toString = path -> new String[]{path.getFileName().toString(), path.toAbsolutePath().toString()};
            return Files.list(graphDirectory)
                    .filter(onlyGraphMlFiles)
                    .map(toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldFindHappyPathForGraphWithOriginalAgent() {
        TinkerCaaGraph graph = new TinkerCaaGraph(this.graphLocation, false);
        CaaExperimentInput experiment = CaaExperimentInput.create(CaaAgentType.ORIGINAL);
        CaaExperimenter caaExperimenter = new CaaExperimenter();

        CaaExperimentResults results = caaExperimenter.executeExperiment(graph, experiment);

        printResultsToConsole(CaaAgentType.ORIGINAL, this.graphFilename, results);
    }

    @Test
    public void shouldFindHappyPathForGraphWithUntraveledEdgeAgent() {
        TinkerCaaGraph graph = new TinkerCaaGraph(this.graphLocation, false);
        CaaExperimentInput experiment = CaaExperimentInput.create(CaaAgentType.UNTRAVELED_EDGE);
        CaaExperimenter caaExperimenter = new CaaExperimenter();

        CaaExperimentResults results = caaExperimenter.executeExperiment(graph, experiment);

        printResultsToConsole(CaaAgentType.UNTRAVELED_EDGE, this.graphFilename, results);
    }

    private void printResultsToConsole(CaaAgentType agentType, String graphFilename,CaaExperimentResults results) {
        System.out.printf(agentType.name() + " CAA Experiment for graph [%s]: " + System.lineSeparator(), graphFilename);
        List<CaaTraversalData> history = results.getHistory();
        System.out.println(String.format(" - %d generations", history.size()));
        int sum = history.stream().mapToInt(d -> d.getTraversedEdges().size()).sum();
        System.out.println(String.format(" - %d edge traversals in total", sum));
        long happyToShortPercent = Math.round(results.getHappyToShortestPathFactor() * 100);
        System.out.println(" - " + happyToShortPercent + "% happy to short factor");
    }
}
