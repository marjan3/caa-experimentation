package com.mtanevski.master.caa.lib.impl.agents;

import com.mtanevski.master.caa.lib.CaaEdge;
import com.mtanevski.master.caa.lib.impl.edges.WeightedCaaEdge;
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

public class OriginalCaaAgentTest {

    @Test
    public void shouldSelectMaxWeightEdge() {
        // given
        WeightedCaaEdge lowestWeightEdge = new WeightedCaaEdge("a", "b", -1.0);
        WeightedCaaEdge middleWeightEdge = new WeightedCaaEdge("b", "c", 0.0);
        WeightedCaaEdge highestWeightEdge = new WeightedCaaEdge("c", "d", 1.0);
        List<CaaEdge> edgesToSelectFrom = Arrays.asList(lowestWeightEdge, middleWeightEdge, highestWeightEdge);

        // when
        OriginalCaaAgent originalCaaAgent = new OriginalCaaAgent();
        Optional<CaaEdge> caaEdge = originalCaaAgent.selectEdge(edgesToSelectFrom);

        // then
        assertTrue(caaEdge.isPresent());
        assertThat(caaEdge.get(), equalTo(highestWeightEdge));
    }

    @Test
    public void shouldSelectRandomlyWhenTwoEdgesMaxWeightsAreEqual() {
        // given
        WeightedCaaEdge lowestWeightEdge = new WeightedCaaEdge("a", "b", -1.0);
        WeightedCaaEdge equalWeightEdge1 = new WeightedCaaEdge("b", "c", 1.0);
        WeightedCaaEdge equalWeightEdge2 = new WeightedCaaEdge("c", "d", 1.0);
        List<CaaEdge> edgesToSelectFrom = Arrays.asList(lowestWeightEdge, equalWeightEdge1, equalWeightEdge2);

        // when
        OriginalCaaAgent originalCaaAgent = new OriginalCaaAgent();
        Optional<CaaEdge> caaEdge = originalCaaAgent.selectEdge(edgesToSelectFrom);

        // then
        assertTrue(caaEdge.isPresent());
        assertThat(caaEdge.get(), anyOf(equalTo(equalWeightEdge1), equalTo(equalWeightEdge2)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotSelectEdgeWhenTheEdgeListIsNull() {
        OriginalCaaAgent originalCaaAgent = new OriginalCaaAgent();
        originalCaaAgent.selectEdge(null);
    }

    @Test
    public void shouldNotSelectEdgeWhenTheEdgeListIsEmpty() {
        // when
        OriginalCaaAgent originalCaaAgent = new OriginalCaaAgent();
        Optional<CaaEdge> caaEdge = originalCaaAgent.selectEdge(Collections.emptyList());

        // then
        assertFalse(caaEdge.isPresent());
    }

}