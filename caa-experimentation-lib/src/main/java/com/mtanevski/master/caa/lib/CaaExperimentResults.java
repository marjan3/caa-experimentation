package com.mtanevski.master.caa.lib;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the collected data from the experiment as it was being executed
 */
public class CaaExperimentResults {

    private final List<String[]> sadnessPaths;
    private final List<String[]> happinessPaths;
    private List<CaaEdge> pathToHappyState;

    private CaaTraversalData current;
    private final List<CaaTraversalData> traversalDataHistory;

    private Double happyToShortestPathFactor;

    public CaaExperimentResults(String start) {
        this.current = new CaaTraversalData(start);
        this.happinessPaths = new ArrayList<>();
        this.sadnessPaths = new ArrayList<>();
        this.traversalDataHistory = new ArrayList<>();
        this.pathToHappyState = new ArrayList<>();
        this.happyToShortestPathFactor = 0.0;
        this.resetCurrent();
    }

    public List<CaaTraversalData> getHistory() {
        return traversalDataHistory;
    }

    public CaaTraversalData current() {
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
        this.current = new CaaTraversalData(this.current.getStart());
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
