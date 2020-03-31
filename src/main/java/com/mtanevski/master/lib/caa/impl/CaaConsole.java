package com.mtanevski.master.lib.caa.impl;

import com.mtanevski.master.lib.caa.CaaAgent;
import com.mtanevski.master.lib.caa.CaaEdge;
import com.mtanevski.master.lib.caa.CaaExperimenter;
import com.mtanevski.master.lib.caa.CaaGraph;
import com.mtanevski.master.lib.caa.impl.agents.CaaAgentFactory;
import com.mtanevski.master.lib.caa.impl.experiment.CaaExperimentData;
import com.mtanevski.master.lib.caa.impl.utils.EfficiencyCalculator;

import java.util.ArrayList;
import java.util.List;

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

    public static void printStart(CaaExperimenter experimenter) {
        System.out.println("The lion starts at: " + experimenter.getData().current().traversedVertex());
        System.out.println("*******************************************************************");
        System.out.println("*******************************************************************");
        System.out.println();
    }

    private static void printAgent(CaaAgentFactory.CaaAgentType agentType) {
        System.out.println("*******************************************************************");
        System.out.println("Using Agent: ");
        System.out.println(agentType.toString());
        System.out.println("*******************************************************************");
    }

    public static void printReachedEndState(CaaExperimenter experimenter) {
        System.out.println("*******************************************************************");
        String vertex = experimenter.getData().current().traversedVertex();
        System.out.println("Lion reached a state at: " + vertex);
        System.out.println("Is state happy: " + experimenter.getTraversedGraph().isVertexHappy(vertex));
        System.out.println("Is state sad: " + experimenter.getTraversedGraph().isVertexSad(vertex));
        System.out.println("*******************************************************************");
        System.out.println("The path the lion took is as follows: " + experimenter.getData().current().traversedVertices());
        System.out.println("*******************************************************************");
    }

    public static void printWalking() {
        System.out.println("The lion walks to adjacent states");
        System.out.println("*******************************************************************");
        System.out.println();
    }

    public static void printState(CaaExperimenter experimenter) {
        System.out.println();
        System.out.println("*******************************************************************");
        String vertex = experimenter.getData().current().traversedVertex();
        System.out.println("The lion went to: " + vertex);
        System.out.println("Previous state: " + experimenter.getData().current().previouslyTraversedVertex());
        System.out.println("Adjacent states: " + experimenter.getTraversedGraph().getAdjacentVertices(vertex));
    }

    public static void printInitialState(CaaExperimenter experimenter, int i) {
        System.out.println();
        System.out.println("*******************************************************************");
        System.out.println("***************************GENERATION " + i + " *******************************");
        System.out.println("*******************************************************************");
        String vertex = experimenter.getData().current().traversedVertex();
        System.out.println("The lion started at: " + vertex);
        System.out.println("Adjacent states: " + experimenter.getTraversedGraph().getAdjacentVertices(vertex));
    }

    private static void printShortestPathEfficiency(CaaExperimenter experimenter) {
        System.out.println("*******************************************************************");
        System.out.println("Shortest Path Efficiency: ");
        System.out.println(experimenter.getData().getHappyToShortestPathFactor() * 100 + "%");
        System.out.println("*******************************************************************");
    }

    public static void printEnd(int counter) {
        System.out.println("*******************************************************************");
        System.out.println("The experiment finished with " + counter + " generation");
        System.out.println("*******************************************************************");
    }

    public static int executeExperimentInDebugMode(CaaExperimenter caaExperimenter) {
        printGraph(caaExperimenter.getInitialGraph());

        printAlgorithmDemonstration();
        printStart(caaExperimenter);
        CaaAgent agent = CaaAgentFactory.getAgent(CaaAgentFactory.CaaAgentType.ADVANCED);
        printAgent(CaaAgentFactory.CaaAgentType.ADVANCED);

        int counter = 0;
        while (!caaExperimenter.pathToHappinessFound()) {
            counter++;
            caaExperimenter.resetTraversal();
            printInitialState(caaExperimenter, counter);
            while (caaExperimenter.canTraverse()) {
                CaaEdge traverse = caaExperimenter.traverse(agent);
                caaExperimenter.trail(traverse);
                printState(caaExperimenter);
                printWalking();
            }
            printReachedEndState(caaExperimenter);
        }

        caaExperimenter.getData().resetCurrent();
        List<CaaEdge> happinessPath = new ArrayList<>();
        while (caaExperimenter.canTraverse()) {
            happinessPath.add(caaExperimenter.traverse(agent));
            CaaEdge lastEdge = happinessPath.get(happinessPath.size() - 1);
            if (caaExperimenter.getTraversedGraph().getHappy().contains(lastEdge.getFrom()) || caaExperimenter.getTraversedGraph().getHappy().contains(lastEdge.getTo())){
                break;
            }
        }
        caaExperimenter.getData().setPathToHappyState(happinessPath);
        caaExperimenter.getData().setHappyToShortestPathFactor(EfficiencyCalculator.calculateHappyToShortestPathFactor(caaExperimenter));

        printGraph(caaExperimenter.getTraversedGraph());
        printShortestPathEfficiency(caaExperimenter);
        printEnd(counter);
        return counter;
    }
}
