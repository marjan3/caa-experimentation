package com.mtanevski.master.caa.lib.impl.graphs.tinker;

// TODO: add unit test for tinker caa graph test

import com.mtanevski.master.caa.lib.CaaEdge;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TinkerCaaGraphTest {

    @Test
    public void shouldFindShortestPath() {
        String graphFileLocation = "src/test/resources/graphs/initial-graph.graphml";
        TinkerCaaGraph graph = new TinkerCaaGraph(graphFileLocation, false);

        List<CaaEdge> shortestPath = graph.getShortestPath("A", "O");

        // then
        List<String> fromVertices = shortestPath.stream().map(CaaEdge::getFrom).collect(Collectors.toList());
        List<String> toVertices = shortestPath.stream().map(CaaEdge::getTo).collect(Collectors.toList());
        assertFalse("Sad vertices should not be part of the shortest path (from)",
                graph.getSad().stream().anyMatch(fromVertices::contains));
        assertFalse("Sad vertices should not be part of the shortest path (to)",
                graph.getSad().stream().anyMatch(toVertices::contains));
        assertEquals(4, shortestPath.size());
    }

    @Test
    public void shouldFindShortestPathBySkippingSadVertices() {
        // given
        String graphFileLocation = "src/test/resources/graphs/6square-graph.graphml";
        TinkerCaaGraph graph = new TinkerCaaGraph(graphFileLocation, false);

        // when
        List<CaaEdge> shortestPath = graph.getShortestPath("n1", "n24");

        // then
        List<String> fromVertices = shortestPath.stream().map(CaaEdge::getFrom).collect(Collectors.toList());
        List<String> toVertices = shortestPath.stream().map(CaaEdge::getTo).collect(Collectors.toList());
        assertFalse("Sad vertices should not be part of the shortest path (from)",
                graph.getSad().stream().anyMatch(fromVertices::contains));
        assertFalse("Sad vertices should not be part of the shortest path (to)",
                graph.getSad().stream().anyMatch(toVertices::contains));
        assertEquals(8, shortestPath.size());

    }
}