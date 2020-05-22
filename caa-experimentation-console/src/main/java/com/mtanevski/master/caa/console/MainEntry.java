package com.mtanevski.master.caa.console;

import com.mtanevski.master.caa.lib.impl.graphs.tinker.TinkerCaaGraph;

public class MainEntry {

    public static final String SAMPLE_GRAPH = "sample-graph.graphml";
    private static final int NUMBER_OF_EXPERIMENTS = 100;

    public static void main(String[] args) {
        CaaConsole.printWelcome();
        for (int i = 0; i < NUMBER_OF_EXPERIMENTS; i++) {
            TinkerCaaGraph tinkerCaaGraph = new TinkerCaaGraph(SAMPLE_GRAPH, false);
            CaaConsole.executeExperimentInDebugMode(tinkerCaaGraph);
            System.out.println();
        }
        System.out.println();
    }

}
