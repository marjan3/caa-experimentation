package com.mtanevski.master.caa.lib;

import java.util.Iterator;
import java.util.List;

public interface CaaGraph {

    List<String> getAllVertices();

    List<CaaEdge> getAllEdges();

    String getStart();

    List<String> getHappy();

    List<String> getSad();

    boolean isVertexHappy(String vertex);

    boolean isVertexSad(String vertex);

    List<String> getAdjacentVertices(String vertex);

    List<CaaEdge> getAdjacentEdges(String vertex);

    void incrementTraversalWeight(Double weight, String from, String to);

    void incrementWeight(Double weight, String from, String to);

    Double getWeight(String from, String to);

    boolean hasHappiness(String from, String to);

    boolean hasSadness(String from, String to);

    CaaGraph immutableClone();

    void resetWeightData();

    void resetTraversalWeightData();

    List<CaaEdge> getShortestPath(String from, String to);

    default Iterator<CaaEdge> iterator(CaaAgent caaAgent) {
        this.resetTraversalWeightData();
        return new CaaTraverseIterator(this, caaAgent);
    }

}
