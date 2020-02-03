package com.mtanevski.master.lib.caa.impl.experiment;

import com.mtanevski.master.fxml.DefaultValues;
import com.mtanevski.master.lib.caa.CaaAgent;
import com.mtanevski.master.lib.caa.CaaEdge;
import com.mtanevski.master.lib.caa.CaaExperimenter;
import com.mtanevski.master.lib.caa.CaaGraph;
import com.mtanevski.master.lib.caa.impl.utils.EfficiencyCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CaaExperimenterImpl implements CaaExperimenter {

    private final CaaGraph initialGraph;
    private CaaExperimentData data;
    private CaaGraph graph;

    private Double happinessIncrement;
    private Double happinessDecrement;

    public CaaExperimenterImpl(CaaGraph caaGraph) {
        this(caaGraph, DefaultValues.HAPPY_INCREMENTER, DefaultValues.SAD_INCREMENTER);
    }

    public CaaExperimenterImpl(CaaGraph caaGraph, Double happyMultiplier, Double sadMultiplier) {
        this.initialGraph = caaGraph.clone();
        this.graph = this.initialGraph.clone();

        this.data = new CaaExperimentData(this.initialGraph.getStart());

        this.happinessIncrement = happyMultiplier;
        this.happinessDecrement = sadMultiplier;
    }

    @Override
    public CaaExperimentData getData() {
        return this.data;
    }

    @Override
    public CaaGraph getTraversedGraph() {
        return this.graph;
    }

    @Override
    public CaaGraph getInitialGraph() {
        return this.initialGraph;
    }

    @Override
    public int executeExperiment(CaaAgent caaAgent) {
        this.graph = this.initialGraph.clone();
        this.data = new CaaExperimentData(this.initialGraph.getStart());
        this.resetTraversal();
        int counter = 0;
        while (!this.pathToHappinessFound()) {
            counter++;
            this.resetTraversal();
            while (this.canTraverse()) {
                CaaEdge traverse = this.traverse(caaAgent);
                this.trail(traverse);
            }
        }
        this.findHappyState(caaAgent);
        return counter;
    }

    @Override
    public void executeTraversal(CaaAgent caaAgent) {
        this.resetTraversal();
        while (this.canTraverse()) {
           this.traverse(caaAgent);
        }
    }

    @Override
    public void resetTraversal() {
        this.data.resetCurrent();
        this.graph.resetTraversalWeightData();
    }

    @Override
    public boolean pathToHappinessFound() {
        // stop traversing when the following condition is met
        List<String[]> pathToHappiness = this.data.getHappinessPaths();
        if (pathToHappiness.size() > 0) {
            int lastItemIndex = pathToHappiness.size() - 1;
            String start = this.graph.getStart();
            String[] lastEdge = pathToHappiness.get(lastItemIndex);
            boolean startIsStartVertex = lastEdge[0].equals(start) || lastEdge[1].equals(start);
            String[] firstEdge = pathToHappiness.get(0);
            boolean isEndHappyVertex = this.graph.getHappy().contains(firstEdge[0]) || this.graph.getHappy().contains(firstEdge[1]);
            return startIsStartVertex && isEndHappyVertex;
        } else {
            return false;
        }
    }

    @Override
    public boolean canTraverse() {
        // stop traversing when the following condition is met
        String vertex = this.data.current().traversedVertex();
        return !(this.graph.isVertexHappy(vertex) || this.graph.isVertexSad(vertex));
    }

    @Override
    public CaaEdge traverse(CaaAgent caaAgent) {
        if (!this.canTraverse()) {
            throw new IllegalStateException("Can not traverse because node already reached happy/sad state.");
        }
        String traversedVertex = this.data.current().traversedVertex();
        String previouslyTraversedVertex = this.data.current().previouslyTraversedVertex();
        // get adjacent edges without the previously traversed vertex so it doesn't enter loop
        List<CaaEdge> adjacent = this.graph.getAdjacentEdges(traversedVertex)
                .stream().filter(edge -> !edge.getTo().equals(previouslyTraversedVertex))
                .collect(Collectors.toList());
        // if there are no adjacent vertices we must return to previously traversed Vertex
        if(adjacent.isEmpty()){
            adjacent = this.graph.getAdjacentEdges(traversedVertex);
        }
        Optional<CaaEdge> pickedEdge = caaAgent.pickEdge(adjacent);
        try{
            CaaEdge caaEdge = pickedEdge.orElse(adjacent.get(0));

            this.data.current().addEdge(new String[]{caaEdge.getFrom(), caaEdge.getTo()});

            return caaEdge;
        }catch (Exception ex){
            System.out.println();
            return null;
        }
    }

    @Override
    public void trail(CaaEdge caaEdge) {
        // incrementing weight to indicate that the edge has been traversed
        this.graph.incrementTraversalWeight(DefaultValues.TRAVERSAL_INCREMENTER, caaEdge.getFrom(), caaEdge.getTo());

        String[] edge = new String[]{caaEdge.getFrom(), caaEdge.getTo()};
        String traversedVertex = caaEdge.getTo();

        // Apply this piece of code if traversal stops when sad state is encountered
        String[] previousEdge = this.data.current().previouslyTraversedEdge();
        if (this.graph.hasSadness(edge[0], edge[1])) {
            if (!this.graph.hasSadness(previousEdge[0], previousEdge[1])) {
                this.data.addToSadness(previousEdge);
                this.graph.incrementWeight(happinessDecrement, previousEdge[0], previousEdge[1]);
                this.data.current().addIncrement(previousEdge, happinessDecrement);
            }
            this.graph.incrementWeight(happinessDecrement, edge[0], edge[1]);
            this.data.current().addIncrement(edge, happinessDecrement);
        }

        if (this.graph.isVertexSad(traversedVertex) && !this.graph.hasSadness(edge[0], edge[1])) {
            this.graph.incrementWeight(happinessDecrement, edge[0], edge[1]);
            this.data.current().addIncrement(edge, happinessDecrement);
            this.data.addToSadness(edge);
        }

        if (this.graph.hasHappiness(edge[0], edge[1])) {
            // if the traversed edge already has happiness index we assume that the previously traversed edge
            if (!this.graph.hasHappiness(previousEdge[0], previousEdge[1])) {
                this.graph.incrementWeight(happinessIncrement, previousEdge[0], previousEdge[1]);
                this.data.current().addIncrement(previousEdge, happinessIncrement);
                this.data.addToHappiness(previousEdge);
            }
            this.graph.incrementWeight(happinessIncrement, edge[0], edge[1]);
            this.data.current().addIncrement(edge, happinessIncrement);
        }

        if (this.graph.isVertexHappy(traversedVertex) && !this.graph.hasHappiness(edge[0], edge[1])) {
            this.graph.incrementWeight(happinessIncrement, edge[0], edge[1]);
            this.data.current().addIncrement(edge, happinessIncrement);
            this.data.addToHappiness(edge);
        }

        if (!this.canTraverse()) {
            this.data.current().setHappy(this.graph.isVertexHappy(traversedVertex));
            this.data.current().setSad(this.graph.isVertexSad(traversedVertex));


            this.data.current().setGraphAsSnapshot(this.graph);
            this.data.save();
        }
    }

    private void findHappyState(CaaAgent caaAgent) {
        this.data.resetCurrent();
        List<CaaEdge> happinessPath = new ArrayList<>();
        while (this.canTraverse()) {
            happinessPath.add(this.traverse(caaAgent));
            CaaEdge lastEdge = happinessPath.get(happinessPath.size() - 1);
            if (this.graph.getHappy().contains(lastEdge.getFrom()) || this.graph.getHappy().contains(lastEdge.getTo())){
                break;
            }
        }
        this.data.setPathToHappyState(happinessPath);
        this.data.setHappyToShortestPathFactor(EfficiencyCalculator.calculateHappyToShortestPathFactor(this));
    }

}
