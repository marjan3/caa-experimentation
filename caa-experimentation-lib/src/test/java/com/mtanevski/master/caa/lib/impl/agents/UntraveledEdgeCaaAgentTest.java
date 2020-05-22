package com.mtanevski.master.caa.lib.impl.agents;

import com.mtanevski.master.caa.lib.CaaEdge;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UntraveledEdgeCaaAgentTest {

    @Test
    public void shouldSelectMaxWeightEdge() {
        // given
        CaaEdge lowestWeightEdge = new CaaEdge("a", "b", -1.0, 0.0);
        CaaEdge middleWeightEdge = new CaaEdge("b", "c", 0.0, -10.0);
        CaaEdge highestWeightEdge = new CaaEdge("c", "d", 11.0, -30.0);
        List<CaaEdge> edgesToSelectFrom = Arrays.asList(lowestWeightEdge, middleWeightEdge, highestWeightEdge);

        // when
        UntraveledEdgeCaaAgent untraveledEdgeCaaAgent = new UntraveledEdgeCaaAgent();
        Optional<CaaEdge> caaEdge = untraveledEdgeCaaAgent.selectEdge(edgesToSelectFrom);

        // then
        assertTrue(caaEdge.isPresent());
        assertThat(caaEdge.get(), equalTo(highestWeightEdge));
    }

    @Test
    public void shouldSelectMaxTraversalWeightEdgeWhenWeightsAreEqual() {
        // given
        CaaEdge lowestTraversalWeightEdge = new CaaEdge("a", "b", 0.0, -10.0);
        CaaEdge middleTraversalWeightEdge = new CaaEdge("b", "c", 0.0, -1.0);
        CaaEdge highestTraversalWeightEdge = new CaaEdge("c", "d", 0.0, 0.0);
        List<CaaEdge> edgesToSelectFrom =
                Arrays.asList(lowestTraversalWeightEdge, middleTraversalWeightEdge, highestTraversalWeightEdge);

        // when
        UntraveledEdgeCaaAgent untraveledEdgeCaaAgent = new UntraveledEdgeCaaAgent();
        Optional<CaaEdge> caaEdge = untraveledEdgeCaaAgent.selectEdge(edgesToSelectFrom);

        // then
        assertTrue(caaEdge.isPresent());
        assertThat(caaEdge.get(), equalTo(highestTraversalWeightEdge));
    }


    @Test
    public void shouldSelectMaxTraversalWeightEdgeWhenWeightsAreEqualAndSomeTraversalWeightsAreEqual() {
        // given
        CaaEdge lowestTraversalWeightEdge = new CaaEdge("a", "b", 0.0, -10.0);
        CaaEdge middleTraversalWeightEdge = new CaaEdge("b", "c", 0.0, -1.0);
        CaaEdge highestTraversalWeightEdge = new CaaEdge("c", "d", 0.0, 0.0);
        List<CaaEdge> edgesToSelectFrom =
                Arrays.asList(lowestTraversalWeightEdge, middleTraversalWeightEdge, highestTraversalWeightEdge);

        // when
        UntraveledEdgeCaaAgent untraveledEdgeCaaAgent = new UntraveledEdgeCaaAgent();
        Optional<CaaEdge> caaEdge = untraveledEdgeCaaAgent.selectEdge(edgesToSelectFrom);

        // then
        assertTrue(caaEdge.isPresent());
        assertThat(caaEdge.get(), equalTo(highestTraversalWeightEdge));
    }

    @Test
    public void shouldSelectRandomlyWhenTwoEdgesMaxWeightsAndMaxTraversalWeightsAreEqual() {
        // given
        CaaEdge lowestTraversalWeightEdge = new CaaEdge("a", "b", -1.0, -2.0);
        CaaEdge equalTraversalWeightEdge1 = new CaaEdge("b", "c", 1.0, 2.0);
        CaaEdge equalTraversalWeightEdge2 = new CaaEdge("c", "d", 1.0, 2.0);
        List<CaaEdge> edgesToSelectFrom
                = Arrays.asList(lowestTraversalWeightEdge, equalTraversalWeightEdge1, equalTraversalWeightEdge2);

        // when
        UntraveledEdgeCaaAgent untraveledEdgeCaaAgent = new UntraveledEdgeCaaAgent();
        Optional<CaaEdge> caaEdge = untraveledEdgeCaaAgent.selectEdge(edgesToSelectFrom);

        // then
        assertTrue(caaEdge.isPresent());
        assertThat(caaEdge.get(), anyOf(equalTo(equalTraversalWeightEdge1), equalTo(equalTraversalWeightEdge2)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotSelectEdgeWhenTheEdgeListIsNull() {
        UntraveledEdgeCaaAgent untraveledEdgeCaaAgent = new UntraveledEdgeCaaAgent();
        untraveledEdgeCaaAgent.selectEdge(null);
    }

    @Test
    public void shouldNotSelectEdgeWhenTheEdgeListIsEmpty() {
        // when
        UntraveledEdgeCaaAgent untraveledEdgeCaaAgent = new UntraveledEdgeCaaAgent();
        Optional<CaaEdge> caaEdge = untraveledEdgeCaaAgent.selectEdge(Collections.emptyList());

        // then
        assertFalse(caaEdge.isPresent());
    }
}