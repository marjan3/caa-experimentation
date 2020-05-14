package com.mtanevski.master.caa.lib;

import com.mtanevski.master.caa.lib.impl.edges.EmptyCaaEdge;
import com.mtanevski.master.caa.lib.impl.edges.SimpleCaaEdge;
import com.mtanevski.master.caa.lib.impl.edges.WeightedCaaEdge;

import java.util.ArrayList;
import java.util.List;

public class CaaTraversalData {

    private final String startingVertexLabel;
    private final List<String> traversedVertices;
    private final List<CaaEdge> traversedEdges;
    private final List<CaaEdge> increments;
    private boolean sad;
    private boolean happy;
    private String traversedVertex;
    private CaaEdge traversedEdge;
    private CaaGraph graph;

    public CaaTraversalData(String startingVertexLabel) {
        this.startingVertexLabel = startingVertexLabel;
        this.traversedVertex = this.startingVertexLabel;
        this.traversedVertices = new ArrayList<>();
        this.traversedVertices.add(this.traversedVertex);

        this.traversedEdge = new EmptyCaaEdge();
        this.traversedEdges = new ArrayList<>();
        this.increments = new ArrayList<>();
        this.happy = false;
        this.sad = false;
    }

    public String getStart() {
        return this.startingVertexLabel;
    }

    public String getTraversedVertex() {
        return this.traversedVertex;
    }

    public List<String> getTraversedVertices() {
        return this.traversedVertices;
    }

    public CaaEdge getTraversedEdge() {
        return this.traversedEdge;
    }

    public List<CaaEdge> getTraversedEdges() {
        return this.traversedEdges;
    }

    public String getLastTraversedVertex() {
        int previousIndex = this.traversedVertices.size() - (this.traversedVertices.size() > 1 ? 2 : 1);
        return this.traversedVertices.get(previousIndex);
    }

    public String[] getLastTraversedEdge() {
        int previousIndex = this.traversedEdges.size() - (this.traversedEdges.size() > 1 ? 2 : 1);
        if (this.getTraversedEdges().size() < 1) {
            return new String[]{null, null};
        }
        CaaEdge caaEdge = this.traversedEdges.get(previousIndex);
        return new String[]{caaEdge.getFrom(), caaEdge.getTo()};
    }

    public void addIncrement(String[] edge, double increment) {
        this.increments.add(WeightedCaaEdge.of(edge[0], edge[1], increment));
    }

    public void addTraversedEdge(String[] edge) {
        this.traversedVertex = edge[1];
        this.traversedVertices.add(this.traversedVertex);

        this.traversedEdge = new SimpleCaaEdge(edge[0], edge[1]);
        this.traversedEdges.add(traversedEdge);
    }

    public boolean isHappy() {
        return this.happy;
    }

    public void setHappy(boolean happy) {
        this.happy = happy;
    }

    public boolean isSad() {
        return this.sad;
    }

    public void setSad(boolean sad) {
        this.sad = sad;
    }

    public List<CaaEdge> getIncrements() {
        return this.increments;
    }

    public CaaGraph getGraph() {
        return graph;
    }

    public void setGraph(CaaGraph graph) {
        this.graph = graph;
    }
}
