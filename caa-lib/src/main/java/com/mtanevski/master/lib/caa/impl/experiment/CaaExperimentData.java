package com.mtanevski.master.lib.caa.impl.experiment;

import com.mtanevski.master.lib.caa.CaaEdge;
import com.mtanevski.master.lib.caa.CaaTraversalData;
import com.mtanevski.master.lib.caa.impl.experiment.traversal.CaaTraversalDataImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the collected data from the experiment as it was being executed
 */
public class CaaExperimentData {

    private final List<String[]> sadnessPaths;
    private final List<String[]> happinessPaths;
    private List<CaaEdge> pathToHappyState;

    private CaaTraversalDataImpl current;
    private final List<CaaTraversalData> traversalDataHistory;

    private Double happyToShortestPathFactor;

    public CaaExperimentData(String start) {
        this.current = new CaaTraversalDataImpl(start);
        this.happinessPaths = new ArrayList<>();
        this.sadnessPaths = new ArrayList<>();
        this.traversalDataHistory = new ArrayList<>();
        this.pathToHappyState = new ArrayList<>();
        this.happyToShortestPathFactor = 0.0;
    }

    public List<CaaTraversalData> getHistory() {
        return traversalDataHistory;
    }

    public CaaTraversalDataImpl current() {
        return current;
    }

    public List<String[]> getSadnessPaths() {
        return sadnessPaths;
    }

    public List<String[]> getHappinessPaths() {
        return happinessPaths;
    }

    public void addToHappiness(String[] edge) {
        this.happinessPaths.add(edge);
    }

    public void addToSadness(String[] edge) {
        this.sadnessPaths.add(edge);
    }

    public void save() {
        this.traversalDataHistory.add(this.current);
    }

    public void resetCurrent() {
        this.current = new CaaTraversalDataImpl(this.current.getStart());
    }

    public Double getHappyToShortestPathFactor() {
        return happyToShortestPathFactor;
    }

    public void setHappyToShortestPathFactor(Double happyToShortestPathFactor) {
        this.happyToShortestPathFactor = happyToShortestPathFactor;
    }

    public List<CaaEdge> getPathToHappyState() {
        return pathToHappyState;
    }

    public void setPathToHappyState(List<CaaEdge> pathToHappyState) {
        this.pathToHappyState = pathToHappyState;
    }
}
