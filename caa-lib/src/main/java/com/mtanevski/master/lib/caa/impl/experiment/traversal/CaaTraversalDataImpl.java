package com.mtanevski.master.lib.caa.impl.experiment.traversal;

import com.mtanevski.master.lib.caa.CaaEdge;
import com.mtanevski.master.lib.caa.CaaGraph;
import com.mtanevski.master.lib.caa.CaaTraversalData;
import com.mtanevski.master.lib.caa.impl.graph.readonly.CaaGraphSnapshot;
import com.mtanevski.master.lib.caa.impl.graph.readonly.EmptyCaaEdge;
import com.mtanevski.master.lib.caa.impl.graph.readonly.WeightedCaaEdge;
import com.mtanevski.master.lib.caa.impl.graph.readonly.SimpleCaaEdge;

import java.util.ArrayList;
import java.util.List;

public class CaaTraversalDataImpl implements CaaTraversalData {

    private final String startingVertexLabel;
    private boolean sad;
    private boolean happy;
    private String traversedVertex;
    private List<String> traversedVertices;

    private CaaEdge traversedEdge;
    private List<CaaEdge> traversedEdges;
    private List<CaaEdge> increments;
    private CaaGraph caaGraph;

    public CaaTraversalDataImpl(String startingVertexLabel) {
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

    @Override
    public String getStart() {
        return this.startingVertexLabel;
    }

    @Override
    public String traversedVertex() {
        return this.traversedVertex;
    }

    @Override
    public List<String> traversedVertices() {
        return this.traversedVertices;
    }

    @Override
    public CaaEdge traversedEdge() {
        return this.traversedEdge;
    }

    @Override
    public List<CaaEdge> traversedEdges() {
        return this.traversedEdges;
    }

    @Override
    public String previouslyTraversedVertex() {
        int previousIndex = this.traversedVertices.size() - (this.traversedVertices.size() > 1 ? 2 : 1);
        return this.traversedVertices.get(previousIndex);
    }

    @Override
    public String[] previouslyTraversedEdge() {
        int previousIndex = this.traversedEdges.size() - (this.traversedEdges.size() > 1 ? 2 : 1);
        if (this.traversedEdges().size() < 1) {
            return new String[]{null, null};
        }
        CaaEdge caaEdge = this.traversedEdges.get(previousIndex);
        return new String[]{caaEdge.getFrom(), caaEdge.getTo()};
    }

    @Override
    public void addIncrement(String[] edge, double increment) {
        this.increments.add(WeightedCaaEdge.of(edge[0], edge[1], increment));
    }

    @Override
    public void addEdge(String[] edge) {
        this.traversedVertex = edge[1];
        this.traversedVertices.add(this.traversedVertex);

        this.traversedEdge = new SimpleCaaEdge(edge[0], edge[1]);
        this.traversedEdges.add(traversedEdge);
    }

    @Override
    public boolean isHappy() {
        return this.happy;
    }

    @Override
    public void setHappy(boolean happy) {
        this.happy = happy;
    }

    @Override
    public boolean isSad() {
        return this.sad;
    }

    @Override
    public void setSad(boolean sad) {
        this.sad = sad;
    }

    public List<CaaEdge> getIncrements() {
        return this.increments;
    }

    @Override
    public void setGraphAsSnapshot(CaaGraph caaGraph) {
        this.caaGraph = new CaaGraphSnapshot(caaGraph);
    }

    @Override
    public CaaGraph getGraph() {
        return this.caaGraph;
    }

}
