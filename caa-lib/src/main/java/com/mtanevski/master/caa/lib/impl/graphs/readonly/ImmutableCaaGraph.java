package com.mtanevski.master.caa.lib.impl.graphs.readonly;

import com.mtanevski.master.caa.lib.CaaEdge;
import com.mtanevski.master.caa.lib.CaaGraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImmutableCaaGraph implements CaaGraph {

    private final List<CaaEdge> edges;
    private final List<String> vertices;
    private final String start;
    private final List<String> happy;
    private final List<String> sad;
    private final Map<String, List<CaaEdge>> shortestPaths;

    public ImmutableCaaGraph(CaaGraph caaGraph) {
        this.vertices = caaGraph.getAllVertices();
        this.edges = caaGraph.getAllEdges();
        this.start = caaGraph.getStart();
        this.happy = caaGraph.getHappy();
        this.sad = caaGraph.getSad();
        this.shortestPaths = new HashMap<>();
        caaGraph.getHappy().forEach(h -> {
            List<CaaEdge> shortestPaths = caaGraph.getShortestPath(this.start, h);
            this.shortestPaths.put(h, shortestPaths);
        });
    }

    @Override
    public List<String> getAllVertices() {
        return this.vertices;
    }

    @Override
    public List<CaaEdge> getAllEdges() {
        return this.edges;
    }

    @Override
    public String getStart() {
        return this.start;
    }

    @Override
    public List<String> getHappy() {
        return this.happy;
    }

    @Override
    public List<String> getSad() {
        return this.sad;
    }

    @Override
    public boolean isVertexHappy(String vertex) {
        return this.getHappy().contains(vertex);
    }

    @Override
    public boolean isVertexSad(String vertex) {
        return this.getSad().contains(vertex);
    }

    @Override
    public List<CaaEdge> getShortestPath(String from, String to) {
        return this.shortestPaths.get(to);
    }

    @Override
    public CaaGraph immutableClone() {
        return new ImmutableCaaGraph(this);
    }

    @Override
    public List<String> getAdjacentVertices(String vertex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<CaaEdge> getAdjacentEdges(String vertex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void incrementTraversalWeight(Double weight, String from, String to) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void incrementWeight(Double weight, String from, String to) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Double getWeight(String from, String to) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasHappiness(String from, String to) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasSadness(String from, String to) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resetWeightData() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resetTraversalWeightData() {
        throw new UnsupportedOperationException();
    }

}
