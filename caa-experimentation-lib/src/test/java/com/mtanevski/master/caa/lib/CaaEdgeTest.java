package com.mtanevski.master.caa.lib;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CaaEdgeTest {

    @Test
    public void shouldCreateCaaEdge() {
        CaaEdge edge = new CaaEdge("a", "b", 10.0, -10.0);

        assertEquals("a", edge.getFrom());
        assertEquals("b", edge.getTo());
        assertEquals(10.0, edge.getWeight(), 0);
        assertEquals(-10.0, edge.getTraversalWeight(), 0);
        assertEquals("{a b}@10.0,-10.0", edge.toString());
    }

}