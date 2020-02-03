package com.mtanevski.master.lib.caa;

import java.util.List;

public interface CaaTraversalData {

    String getStart();

    String traversedVertex();

    List<String> traversedVertices();

    CaaEdge traversedEdge();

    List<CaaEdge> traversedEdges();

    String previouslyTraversedVertex();

    String[] previouslyTraversedEdge();

    void addIncrement(String[] edge, double increment);

    void addEdge(String[] edge);

    boolean isHappy();

    void setHappy(boolean happy);

    boolean isSad();

    void setSad(boolean sad);

    List<CaaEdge> getIncrements();

    void setGraphAsSnapshot(CaaGraph caaGraph);

    CaaGraph getGraph();
}
