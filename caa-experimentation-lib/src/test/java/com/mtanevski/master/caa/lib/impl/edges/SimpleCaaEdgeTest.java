package com.mtanevski.master.caa.lib.impl.edges;

import com.mtanevski.master.caa.lib.CaaEdge;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimpleCaaEdgeTest {

    @Test
    public void shouldCreateSimpleCaaEdge() {
        CaaEdge edge = SimpleCaaEdge.of("a", "b");

        assertEquals("a", edge.getFrom());
        assertEquals("b", edge.getTo());
        assertEquals(0.0, edge.getWeight(), 0);
        assertEquals(0.0, edge.getTraversalWeight(), 0);
    }
}