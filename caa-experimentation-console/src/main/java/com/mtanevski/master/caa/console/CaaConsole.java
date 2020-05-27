package com.mtanevski.master.caa.console;

import com.mtanevski.master.caa.lib.CaaAgent;
import com.mtanevski.master.caa.lib.CaaEdge;
import com.mtanevski.master.caa.lib.CaaExperimentResults;
import com.mtanevski.master.caa.lib.CaaExperimenter;
import com.mtanevski.master.caa.lib.CaaGraph;
import com.mtanevski.master.caa.lib.Constants;
import com.mtanevski.master.caa.lib.impl.agents.CaaAgentFactory;
import com.mtanevski.master.caa.lib.impl.agents.CaaAgentType;

import java.util.Iterator;

public class CaaConsole {

    public static void printGraph(CaaGraph graph) {
        System.out.println("*******************************************************************");
        System.out.println("State of the Graph: ");
        System.out.println(graph.toString());
        System.out.println("*******************************************************************");
        System.out.println("Happy states: " + graph.getHappy());
        System.out.println("Sad states: " + graph.getSad());
    }

    public static void printWelcome() {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("*******************************************************************");
        System.out.println("Welcome to CAA!- You are a lion trying to escape the jungle maze...");
        System.out.println("*******************************************************************");
        System.out.println();
        System.out.println();
        System.out.println();
    }

    public static void printAlgorithmDemonstration() {
        System.out.println("*******************************************************************");
        System.out.println("*******************************************************************");
        System.out.println("Algorithm Demonstration...");
    }

    public static void printStart(CaaExperimentResults data, CaaAgentType agentType) {
        System.out.println("The lion starts at: " + data.current().getTraversedVertex());
        System.out.println("*******************************************************************");
        System.out.println("*******************************************************************");
        System.out.println("Using Agent: ");
        System.out.println(agentType);
        System.out.println("*******************************************************************");
        System.out.println();
    }

    public static void printReachedEndState(CaaExperimentResults data, CaaGraph graph) {
        System.out.println("*******************************************************************");
        String vertex = data.current().getTraversedVertex();
        System.out.println("Lion reached a state at: " + vertex);
        System.out.println("Is state happy: " + graph.isVertexHappy(vertex));
        System.out.println("Is state sad: " + graph.isVertexSad(vertex));
        System.out.println("*******************************************************************");
        System.out.println("The path the lion took is as follows: " + data.current().getTraversedVertices());
        System.out.println("*******************************************************************");
    }

    public static void printWalking() {
        System.out.println("The lion walks to adjacent states");
        System.out.println("*******************************************************************");
        System.out.println();
    }

    public static void printState(CaaExperimentResults data, CaaGraph graph) {
        System.out.println();
        System.out.println("*******************************************************************");
        String vertex = data.current().getTraversedVertex();
        System.out.println("The lion went to: " + vertex);
        System.out.println("Previous state: " + data.current().getLastTraversedVertex());
        System.out.println("Adjacent states: " + graph.getAdjacentVertices(vertex));
    }

    public static void printInitialState(CaaExperimentResults data, CaaGraph caaGraph, int i) {
        System.out.println();
        System.out.println("*******************************************************************");
        System.out.println("***************************GENERATION " + i + " *******************************");
        System.out.println("*******************************************************************");
        String vertex = data.current().getTraversedVertex();
        System.out.println("The lion started at: " + vertex);
        System.out.println("Adjacent states: " + caaGraph.getAdjacentVertices(vertex));
    }

    private static void printShortestPathEfficiency(CaaExperimentResults data) {
        System.out.println("*******************************************************************");
        System.out.println("Shortest Path Efficiency: ");
        System.out.println(data.getHappyToShortestPathFactor() * 100 + "%");
        System.out.println("*******************************************************************");
    }

    public static void printEnd(int counter) {
        System.out.println("*******************************************************************");
        System.out.println("The experiment finished with " + counter + " generation");
        System.out.println("*******************************************************************");
    }

    public static void executeExperimentInDebugMode(CaaGraph graph) {
        printGraph(graph);

        printAlgorithmDemonstration();
        CaaExperimenter caaExperimenter = new CaaExperimenter();
        CaaExperimentResults results = new CaaExperimentResults(graph.getStart());
        CaaAgentType agentType = CaaAgentType.UNTRAVELED_EDGE;
        CaaAgent agent = CaaAgentFactory.getAgent(agentType);
        printStart(results, agentType);

        int counter = 0;
        while (caaExperimenter.pathToHappinessNotFound(graph, results)) {
            counter++;
            results.resetCurrent();
            Iterator<CaaEdge> iterator = graph.iterator(agent);
            printInitialState(results, graph, counter);
            while (iterator.hasNext()) {
                CaaEdge traversedEdge = iterator.next();
                caaExperimenter.trailEdge(traversedEdge, graph, results);
                caaExperimenter.trailCaaDataForEdge(traversedEdge, graph,
                        Constants.HAPPY_INCREMENT, Constants.SAD_INCREMENT, results
                );
                printState(results, graph);
                printWalking();
            }
            printReachedEndState(results, graph);
        }
        caaExperimenter.trailPathToHappyState(agent, graph, results);

        printGraph(graph);
        printShortestPathEfficiency(results);
        printEnd(results.getHistory().size());
    }
}
