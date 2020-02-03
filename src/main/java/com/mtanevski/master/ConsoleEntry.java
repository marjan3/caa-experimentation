package com.mtanevski.master;

import com.mtanevski.master.lib.caa.CaaExperimenter;
import com.mtanevski.master.lib.caa.impl.CaaConsole;
import com.mtanevski.master.lib.caa.impl.experiment.CaaExperimenterImpl;
import com.mtanevski.master.lib.caa.impl.graph.tinker.TinkerCaaGraph;

public class ConsoleEntry {

    private static final int NUMBER_OF_EXPERIMENTS = 100;

    public static void main(String[] args) {

        CaaConsole.printWelcome();

        for (int i = 0; i < NUMBER_OF_EXPERIMENTS; i++) {

            TinkerCaaGraph tinkerCaaGraph = new TinkerCaaGraph("sample-graph.graphml", false);
            CaaExperimenter caaExperimenter = new CaaExperimenterImpl(tinkerCaaGraph);


            CaaConsole.executeExperimentInDebugMode(caaExperimenter);
            System.out.println();
        }

        System.out.println();

    }


}
