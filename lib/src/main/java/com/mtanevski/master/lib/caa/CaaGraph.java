package com.mtanevski.master.lib.caa;

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

    CaaGraph clone();

    void resetWeightData();

    void resetTraversalWeightData();

    List<CaaEdge> getShortestPath(String from, String to);
}
