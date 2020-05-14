package com.mtanevski.master.caa.lib;

import com.mtanevski.master.caa.lib.impl.agents.CaaAgentFactory;
import com.mtanevski.master.caa.lib.impl.edges.SimpleCaaEdge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public final class CaaExperimenter {

    public static CaaExperimentResults execute(CaaGraph graph, CaaExperimentInput caaExperimentInput) {
        CaaAgent agent = CaaAgentFactory.getAgent(caaExperimentInput.getAgentType());
        CaaExperimentResults results = new CaaExperimentResults(graph.getStart());

        results.resetCurrent();
        graph.resetTraversalWeightData();

        while (CaaExperimenter.pathToHappinessNotFound(graph, results)) {
            results.resetCurrent();
            Iterator<CaaEdge> iterator = graph.iterator(agent);
            while (iterator.hasNext()) {
                CaaEdge traversedEdge = iterator.next();
                CaaExperimenter.trailEdge(traversedEdge, results);
                CaaExperimenter.trailCaaDataForEdge(
                        traversedEdge,
                        graph,
                        caaExperimentInput.getIncrement(),
                        caaExperimentInput.getDecrement(),
                        results);
            }
        }
        CaaExperimenter.trailPathToHappyState(agent, graph, results);
        CaaExperimenter.trailHappyToShortestPathFactor(graph, results);
        return results;
    }

    public static boolean pathToHappinessNotFound(
            CaaGraph graph,
            CaaExperimentResults results) {
        // stop traversing when the following condition is met
        List<String[]> pathToHappiness = results.getHappinessPaths();
        if (pathToHappiness.size() > 0) {
            String start = graph.getStart();
            String[] lastEdge = pathToHappiness.get(pathToHappiness.size() - 1);
            boolean startIsStartVertex = lastEdge[0].equals(start) || lastEdge[1].equals(start);
            String[] firstEdge = pathToHappiness.get(0);
            boolean isEndHappyVertex = graph.getHappy().contains(firstEdge[0]) || graph.getHappy().contains(firstEdge[1]);
            return !startIsStartVertex || !isEndHappyVertex;
        } else {
            return true;
        }
    }

    public static void trailPathToHappyState(
            CaaAgent agent,
            CaaGraph graph,
            CaaExperimentResults results) {
        List<CaaEdge> happinessPath = new ArrayList<>();
        Iterator<CaaEdge> iterator = graph.iterator(agent);
        while (iterator.hasNext()) {
            happinessPath.add(iterator.next());
            CaaEdge lastEdge = happinessPath.get(happinessPath.size() - 1);
            if (graph.getHappy().contains(lastEdge.getFrom()) || graph.getHappy().contains(lastEdge.getTo())) {
                break;
            }
        }
        results.setPathToHappyState(happinessPath);
    }

    public static void trailHappyToShortestPathFactor(
            CaaGraph graph,
            CaaExperimentResults results) {
        double happyToShortestPathFactor = 0;
        List<CaaEdge> pathToHappyState = results.getPathToHappyState();
        if (pathToHappyState.size() > 0) {
            CaaEdge lastEdge = pathToHappyState.get(pathToHappyState.size() - 1);
            String happy = lastEdge.getFrom();
            happy = graph.getHappy().contains(happy) ? happy : lastEdge.getTo();
            List<CaaEdge> shortestPath = graph.getShortestPath(graph.getStart(), happy);
            List<CaaEdge> shortestHappyPath = pathToHappyState.stream()
                    .filter(edge ->
                            shortestPath.contains(edge) ||
                                    shortestPath.contains(SimpleCaaEdge.of(edge.getTo(), edge.getFrom())))
                    .collect(Collectors.toList());
            happyToShortestPathFactor = shortestHappyPath.size() / (1d * shortestPath.size());
        }

        results.setHappyToShortestPathFactor(happyToShortestPathFactor);
    }

    public static void trailEdge(CaaEdge caaEdge,
                                 CaaExperimentResults results) {

        String[] edge = new String[]{caaEdge.getFrom(), caaEdge.getTo()};

        results.current().addTraversedEdge(edge);
    }

    public static void trailCaaDataForEdge(CaaEdge caaEdge,
                                           CaaGraph graph,
                                           double increment,
                                           double decrement,
                                           CaaExperimentResults results) {

        String[] edge = new String[]{caaEdge.getFrom(), caaEdge.getTo()};

        // incrementing weight to indicate that the edge has been traversed
        graph.incrementTraversalWeight(Constants.TRAVERSAL_INCREMENTER, caaEdge.getFrom(), caaEdge.getTo());

        String traversedVertex = caaEdge.getTo();

        // Apply this piece of code if traversal stops when sad state is encountered
        String[] previousEdge = results.current().getLastTraversedEdge();
        if (graph.hasSadness(edge[0], edge[1])) {
            if (!graph.hasSadness(previousEdge[0], previousEdge[1])) {
                results.addToSadness(previousEdge);
                graph.incrementWeight(decrement, previousEdge[0], previousEdge[1]);
                results.current().addIncrement(previousEdge, decrement);
            }
            graph.incrementWeight(decrement, edge[0], edge[1]);
            results.current().addIncrement(edge, decrement);
        }

        if (graph.isVertexSad(traversedVertex) && !graph.hasSadness(edge[0], edge[1])) {
            graph.incrementWeight(decrement, edge[0], edge[1]);
            results.current().addIncrement(edge, decrement);
            results.addToSadness(edge);
        }

        if (graph.hasHappiness(edge[0], edge[1])) {
            // if the traversed edge already has happiness index we assume that the previously traversed edge
            if (!graph.hasHappiness(previousEdge[0], previousEdge[1])) {
                graph.incrementWeight(increment, previousEdge[0], previousEdge[1]);
                results.current().addIncrement(previousEdge, increment);
                results.addToHappiness(previousEdge);
            }
            graph.incrementWeight(increment, edge[0], edge[1]);
            results.current().addIncrement(edge, increment);
        }

        if (graph.isVertexHappy(traversedVertex) && !graph.hasHappiness(edge[0], edge[1])) {
            graph.incrementWeight(increment, edge[0], edge[1]);
            results.current().addIncrement(edge, increment);
            results.addToHappiness(edge);
        }

        if (graph.isVertexHappy(traversedVertex) || graph.isVertexSad(traversedVertex)) {
            results.current().setHappy(graph.isVertexHappy(traversedVertex));
            results.current().setSad(graph.isVertexSad(traversedVertex));

            results.current().setGraph(graph.immutableClone());
            results.save();
        }

    }


}
