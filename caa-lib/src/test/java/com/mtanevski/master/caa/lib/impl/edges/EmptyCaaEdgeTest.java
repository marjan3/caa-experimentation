package com.mtanevski.master.caa.lib.impl.edges;

import com.mtanevski.master.caa.lib.CaaEdge;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EmptyCaaEdgeTest {

    @Test
    public void shouldCreateEmptyCaaEdge() {
        CaaEdge edge = new EmptyCaaEdge();

        assertNull(edge.getFrom());
        assertNull(edge.getTo());
        assertEquals(0.0, edge.getWeight(), 0);
        assertEquals(0.0, edge.getTraversalWeight(), 0);
    }
}