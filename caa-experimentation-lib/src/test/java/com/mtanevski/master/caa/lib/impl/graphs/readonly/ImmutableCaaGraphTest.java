package com.mtanevski.master.caa.lib.impl.graphs.readonly;

import com.mtanevski.master.caa.lib.impl.graphs.tinker.TinkerCaaGraph;
import org.junit.Test;

import static com.mtanevski.hamcrest.matchers.FailWithMatcher.failsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class ImmutableCaaGraphTest {

    private static final String SAMPLE_GRAPH = "src/test/resources/graphs/initial-graph.graphml";

    @Test
    public void shouldCreateDataSnapshotOutOfExistingCaaGraph() {
        // given
        TinkerCaaGraph existingCaaGraph = new TinkerCaaGraph(SAMPLE_GRAPH, false);

        // when
        ImmutableCaaGraph immutableCaaGraph = new ImmutableCaaGraph(existingCaaGraph);

        // then
        assertThat(immutableCaaGraph.getStart(), equalTo(existingCaaGraph.getStart()));
        assertThat(immutableCaaGraph.getHappy(), equalTo(existingCaaGraph.getHappy()));
        assertThat(immutableCaaGraph.getSad(), equalTo(existingCaaGraph.getSad()));
        assertThat(immutableCaaGraph.getAllVertices(), equalTo(existingCaaGraph.getAllVertices()));
        assertThat(immutableCaaGraph.getAllEdges(), equalTo(existingCaaGraph.getAllEdges()));
        assertThat(immutableCaaGraph.getShortestPath("A", "O"), equalTo(existingCaaGraph.getShortestPath("A", "O")));
        assertThat(() -> immutableCaaGraph.getAdjacentEdges("A"), failsWith(UnsupportedOperationException.class));
        assertThat(() -> immutableCaaGraph.getAdjacentVertices("A"), failsWith(UnsupportedOperationException.class));
        assertThat(() -> immutableCaaGraph.getWeight("A", "B"), failsWith(UnsupportedOperationException.class));
        assertThat(immutableCaaGraph::resetWeightData, failsWith(UnsupportedOperationException.class));
        assertThat(immutableCaaGraph::resetTraversalWeightData, failsWith(UnsupportedOperationException.class));
        assertThat(() -> immutableCaaGraph.incrementWeight(1.0, "A", "B"),
                failsWith(UnsupportedOperationException.class));
        assertThat(() -> immutableCaaGraph.incrementTraversalWeight(1.0, "A", "B"),
                failsWith(UnsupportedOperationException.class));
        assertThat(() -> immutableCaaGraph.hasSadness("A", "B"), failsWith(UnsupportedOperationException.class));
        assertThat(() -> immutableCaaGraph.hasHappiness("A", "B"), failsWith(UnsupportedOperationException.class));
        assertNotNull(immutableCaaGraph.immutableClone());
        assertNotEquals(immutableCaaGraph.hashCode(), immutableCaaGraph.immutableClone().hashCode());
        assertThat(immutableCaaGraph.isVertexHappy("O"), equalTo(existingCaaGraph.isVertexHappy("O")));
        assertThat(immutableCaaGraph.isVertexSad("N"), equalTo(existingCaaGraph.isVertexSad("N")));
    }

}