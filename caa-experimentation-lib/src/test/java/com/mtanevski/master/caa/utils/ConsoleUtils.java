package com.mtanevski.master.caa.utils;

import com.mtanevski.master.caa.lib.CaaEdge;
import com.mtanevski.master.caa.lib.CaaExperimentResults;
import com.mtanevski.master.caa.lib.CaaGraph;
import com.mtanevski.master.caa.lib.CaaTraversalData;
import com.mtanevski.master.caa.lib.impl.agents.CaaAgentType;
import com.mtanevski.master.caa.lib.impl.edges.SimpleCaaEdge;
import com.mtanevski.master.caa.lib.impl.graphs.tinker.TinkerCaaGraph;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.join;

public class ConsoleUtils {

    public static void printResultsToConsole(TinkerCaaGraph caaGraph,
                                             CaaAgentType agentType,
                                             String graphFilename,
                                             CaaExperimentResults results) {
        System.out.printf(agentType.name() + " CAA Experiment for graph [%s]: \n", graphFilename);
        List<CaaTraversalData> history = results.getHistory();
        System.out.printf(" - %d generations%n", history.size());
        int sum = history.stream().mapToInt(d -> d.getTraversedEdges().size()).sum();
        System.out.printf(" - %d edge traversals in total%n", sum);
        long happyToShortPercent = Math.round(results.getHappyToShortestPathFactor() * 100);
        System.out.println(" - " + happyToShortPercent + "% happy to short factor");
        System.out.println(" - " + caaGraph.getAllVertices().size() + " vertices in total");
        System.out.println(" - " + caaGraph.getAllEdges().size() + " edges in total");
        System.out.println(" - " + caaGraph.getHappy().size() + " happy edges in total");
        System.out.println(" - " + caaGraph.getSad().size() + " sad edges in total");
    }

    public static String getGraphDetailsString(String graphFilename, CaaGraph graph, CaaExperimentResults results) {
        final List<CaaEdge> pathToHappyState = results.getPathToHappyState();
        final CaaEdge happyEdge = pathToHappyState.get(pathToHappyState.size() - 1);
        String happyVertex = happyEdge.getTo();
        if (graph.isVertexHappy(happyEdge.getFrom())) {
            happyVertex = happyEdge.getFrom();
        }
        final List<CaaEdge> shortestPath = graph.getShortestPath(graph.getStart(), happyVertex);
        return "\n\nThe graph " + graphFilename + " has the following information about its structure: \n" +
                "   - number of vertices: " + graph.getAllVertices().size() + "\n" +
                "   - number of edges: " + graph.getAllEdges().size() + "\n" +
                "   - start vertex: " + graph.getStart() + "\n" +
                "   - \"sad\" vertices: " + join(graph.getSad(), ",") + "\n" +
                "   - \"happy\" vertices: " + join(graph.getHappy(), ",") + "\n" +
                "   - shortest path edges (from " + graph.getStart() + " to " + happyVertex + "): "
                + join(toSimpleCaaEdgeList(shortestPath), ",") + "\n" +
                "   - shortest path size: " + shortestPath.size() + "\n\n";
    }

    public static String getExperimentsDescriptionString() {
        return "For the given graph 30 experiments were executed. " +
                "The results of the experiments are presented in a tabular form below. " +
                "The row marked with yellow is the experiment where displayed visually in Appendix B." +
                "At the end of the table a statistical average was calculated for the 30 graphs.\n";
    }

    public static String getExperimentResultsAsStringTableHeaders() {
        return "#|" +
                "\"Happy\" path|" +
                "\"Happy\" path size|" +
                "\"Happy\" to shortest path factor (percentage)\n";
    }

    public static String getExperimentResultsAsStringTableRow(int trial, CaaExperimentResults results) {
        StringBuilder builder = new StringBuilder();

        final List<CaaEdge> pathToHappyState = results.getPathToHappyState();
        builder.append(trial).append("|");
        builder.append(join(toSimpleCaaEdgeList(pathToHappyState), ",")).append("|");
        builder.append(pathToHappyState.size()).append("|");
        final String happyToShortestPathFactor = String.format("%.2f", results.getHappyToShortestPathFactor() * 100);
        builder.append(happyToShortestPathFactor).append("%").append("\n");

        return builder.toString();
    }

    private static List<SimpleCaaEdge> toSimpleCaaEdgeList(List<? extends CaaEdge> caaEdges) {
        return caaEdges.stream().map(SimpleCaaEdge::new).collect(Collectors.toList());
    }

}
