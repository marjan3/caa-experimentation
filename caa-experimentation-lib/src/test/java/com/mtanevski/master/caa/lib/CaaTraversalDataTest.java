package com.mtanevski.master.caa.lib;

import com.mtanevski.master.caa.lib.impl.edges.EmptyCaaEdge;
import com.mtanevski.master.caa.lib.impl.edges.SimpleCaaEdge;
import com.mtanevski.master.caa.lib.impl.edges.WeightedCaaEdge;
import com.mtanevski.master.caa.lib.impl.graphs.tinker.TinkerCaaGraph;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class CaaTraversalDataTest {

    private static final String SAMPLE_GRAPH = "src/test/resources/graphs/initial-graph.graphml";

    @Test
    public void shouldCreateCaaTraversalData() {
        CaaTraversalData caaTraversalData = new CaaTraversalData("A");

        assertNull(caaTraversalData.getGraph());
        assertThat(caaTraversalData.getStart(), is("A"));
        assertThat(caaTraversalData.isHappy(), is(false));
        assertThat(caaTraversalData.isSad(), is(false));
        assertThat(caaTraversalData.getTraversedEdge(), CoreMatchers.is(new EmptyCaaEdge()));
        assertThat(caaTraversalData.getTraversedVertex(), is("A"));
        assertThat(caaTraversalData.getTraversedVertices(), hasItems("A"));
        assertThat(caaTraversalData.getLastTraversedEdge(), is(new String[]{null, null}));
        assertThat(caaTraversalData.getLastTraversedVertex(), is("A"));
    }


    @Test
    public void shouldSetGraphAsSnapshotFromExistingCaaGraph() {
        // given
        CaaTraversalData caaTraversalData = new CaaTraversalData("A");

        // when
        caaTraversalData.setGraph(new TinkerCaaGraph(SAMPLE_GRAPH, false));

        // then
        assertNotNull(caaTraversalData.getGraph());
    }

    @Test
    public void shouldAddTraversedEdgeToExistingCaaTraversalData() {
        // given
        CaaTraversalData caaTraversalData = new CaaTraversalData("A");

        // when
        caaTraversalData.addTraversedEdge(new String[]{"A", "B"});

        // then
        assertThat(caaTraversalData.getTraversedEdge(), is(new SimpleCaaEdge("A", "B")));
        assertThat(caaTraversalData.getTraversedEdges(), hasItems(new SimpleCaaEdge("A", "B")));
        assertThat(caaTraversalData.getTraversedVertices(), hasItems("A"));
        assertThat(caaTraversalData.getTraversedVertex(), is("B"));
    }

    @Test
    public void shouldAddIncrementToCaaTraversalData() {
        // given
        CaaTraversalData caaTraversalData = new CaaTraversalData("A");

        // when
        caaTraversalData.addIncrement(new String[]{"A", "B"}, 1.0);

        // then
        assertThat(caaTraversalData.getIncrements(), CoreMatchers.hasItems(new WeightedCaaEdge("A", "B", 1.0)));
    }

    @Test
    public void shouldGetLastTraversedEdgeSameAsExistingTraversedEdge() {
        // given
        CaaTraversalData caaTraversalData = new CaaTraversalData("A");

        // when
        caaTraversalData.addTraversedEdge(new String[]{"A", "B"});
        String[] lastTraversedEdge = caaTraversalData.getLastTraversedEdge();

        // then
        assertThat(lastTraversedEdge, is(new String[]{"A", "B"}));
    }


    @Test
    public void shouldGetSecondLastTraversedEdgeFromTwoTraversedEdges() {
        // given
        CaaTraversalData caaTraversalData = new CaaTraversalData("A");

        // when
        caaTraversalData.addTraversedEdge(new String[]{"A", "B"});
        caaTraversalData.addTraversedEdge(new String[]{"B", "C"});
        String[] lastTraversedEdge = caaTraversalData.getLastTraversedEdge();
        caaTraversalData.addTraversedEdge(new String[]{"C", "D"});

        // then
        assertThat(lastTraversedEdge, is(new String[]{"A", "B"}));
    }


    @Test
    public void shouldGetLastTraversedVertexSameAsPropertyFromAtExistingTraversedVertex() {
        // given
        CaaTraversalData caaTraversalData = new CaaTraversalData("A");

        // when
        caaTraversalData.addTraversedEdge(new String[]{"A", "B"});
        String lastTraversedVertex = caaTraversalData.getLastTraversedVertex();

        // then
        assertThat(lastTraversedVertex, is("A"));
    }

    @Test
    public void shouldGetSecondLastTraversedVertexFromPropertyFromWithTwoTraversedVertices() {
        // given
        CaaTraversalData caaTraversalData = new CaaTraversalData("A");

        // when
        caaTraversalData.addTraversedEdge(new String[]{"A", "B"});
        caaTraversalData.addTraversedEdge(new String[]{"B", "C"});
        String lastTraversedVertex = caaTraversalData.getLastTraversedVertex();
        caaTraversalData.addTraversedEdge(new String[]{"C", "D"});

        // then
        assertThat(lastTraversedVertex, is("B"));
    }

    @Test
    public void shouldSetHappy() {
        // given
        CaaTraversalData caaTraversalData = new CaaTraversalData("A");

        // when
        caaTraversalData.setHappy(true);

        // then
        assertTrue(caaTraversalData.isHappy());
    }


    @Test
    public void shouldSetSad() {
        // given
        CaaTraversalData caaTraversalData = new CaaTraversalData("A");

        // when
        caaTraversalData.setSad(true);

        // then
        assertTrue(caaTraversalData.isSad());
    }

}