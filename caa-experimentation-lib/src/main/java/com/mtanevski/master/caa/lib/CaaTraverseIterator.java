package com.mtanevski.master.caa.lib;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CaaTraverseIterator implements Iterator<CaaEdge> {

    private final CaaAgent agent;
    private final CaaGraph graph;
    private final List<String> traversedVertices;

    public CaaTraverseIterator(CaaGraph caaGraph, CaaAgent caaAgent) {
        this.graph = caaGraph;
        this.agent = caaAgent;
        this.traversedVertices = new ArrayList<>();
        this.traversedVertices.add(caaGraph.getStart());
    }

    @Override
    public boolean hasNext() {
        // stop traversing when the following condition is met
        String vertex = getTraversedVertex();
        return !(this.graph.isVertexHappy(vertex) || this.graph.isVertexSad(vertex));
    }

    @Override
    public CaaEdge next() {
        if (!this.hasNext()) {
            throw new IllegalStateException("Can not traverse because node already reached happy/sad state.");
        }
        String traversedVertex = getTraversedVertex();
        String previouslyTraversedVertex = getPreviouslyTraversedVertex();
        // get adjacent edges without the previously traversed vertex so it doesn't enter loop
        List<CaaEdge> adjacent = this.graph.getAdjacentEdges(traversedVertex)
                .stream().filter(edge -> !edge.getTo().equals(previouslyTraversedVertex))
                .collect(Collectors.toList());
        // if there are no adjacent vertices we must return to previously traversed Vertex
        if (adjacent.isEmpty()) {
            adjacent = this.graph.getAdjacentEdges(traversedVertex);
        }
        Optional<CaaEdge> pickedEdge = this.agent.selectEdge(adjacent);
        try {
            CaaEdge caaEdge = pickedEdge.orElse(adjacent.get(0));
            this.traversedVertices.add(caaEdge.getTo());

            return caaEdge;
        } catch (Exception ex) {
            return null;
        }
    }

    private String getTraversedVertex() {
        return this.traversedVertices.get(this.traversedVertices.size() - 1);
    }

    private String getPreviouslyTraversedVertex() {
        if (traversedVertices.size() < 2) {
            return this.getTraversedVertex();
        }
        return this.traversedVertices.get(this.traversedVertices.size() - 2);
    }
}
