package com.mtanevski.master.lib.caa.impl.utils;

import com.mtanevski.master.lib.caa.CaaEdge;
import com.mtanevski.master.lib.caa.CaaExperimenter;
import com.mtanevski.master.lib.caa.CaaGraph;
import com.mtanevski.master.lib.caa.impl.graph.readonly.SimpleCaaEdge;

import java.util.List;
import java.util.stream.Collectors;

public class EfficiencyCalculator {

    public static double calculateHappyToShortestPathFactor(CaaExperimenter caaExperimenter){
        CaaGraph caaGraph = caaExperimenter.getData().getHistory().get(0).getGraph();
        List<CaaEdge> pathToHappyState = caaExperimenter.getData().getPathToHappyState();
        if(pathToHappyState.size() > 0) {
            CaaEdge lastEdge = pathToHappyState.get(pathToHappyState.size() - 1);
            String happy = lastEdge.getFrom();
            happy = caaGraph.getHappy().contains(happy) ? happy : lastEdge.getTo();
            List<CaaEdge> shortestPath = caaGraph.getShortestPath(caaGraph.getStart(), happy);
            List<CaaEdge> shortestHappyPath = pathToHappyState.stream()
                    .filter(edge ->
                            shortestPath.contains(edge) ||
                                    shortestPath.contains(SimpleCaaEdge.of(edge.getTo(), edge.getFrom())))
                    .collect(Collectors.toList());
            return shortestHappyPath.size() / (1d * shortestPath.size());
        } else {
            // Can't calculate happy to shortest path factor because path to happiness is not found
            return 0;
        }
    }
}
