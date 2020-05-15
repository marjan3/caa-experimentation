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
    public void shouldFindHappyPathForGraph() {
        TinkerCaaGraph graph = new TinkerCaaGraph(this.graphLocation, false);
        CaaExperimentInput experiment = CaaExperimentInput.create(CaaAgentType.UNTRAVELED_EDGE);

        CaaExperimentResults results = CaaExperimenter.executeExperiment(graph, experiment);

        System.out.printf("Experiment for graph [%s]: " + System.lineSeparator(), this.graphFilename);
        System.out.println(" - " + results.getHistory().size() + " generations");
        System.out.println(" - " + Math.round(results.getHappyToShortestPathFactor() * 100) + "% happy to short factor");
    }
}
