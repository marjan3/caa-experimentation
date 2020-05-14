package com.mtanevski.master.caa.lib;

import org.apache.tinkerpop.gremlin.process.traversal.IO;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class TinkerGraphTryoutTest {

    @Test
    public void testInitialGraph() {
        System.out.println("BasicGraph Traversal Test");

        Graph graph = TinkerGraph.open();
        GraphTraversalSource g = graph.traversal();

        g.io("src/test/resources/graphs/initial-graph.graphml").
                with(IO.reader, IO.graphml).
                read().iterate();

        assertEquals("graphtraversalsource[tinkergraph[vertices:17 edges:33], standard]", g.toString());
        g.V().has("label", "A").next().vertices(Direction.BOTH).forEachRemaining(vertex -> {
            System.out.println(vertex);
            vertex.property("type", "happy");
        });
        assertNotNull(g.V().has("label", P.eq("A")).next().vertices(Direction.BOTH));
        graph.traversal().V().has("label", P.eq("A")).next().vertices(Direction.BOTH)
                .forEachRemaining(vertex -> assertEquals("happy", vertex.property("type").value()));
        System.out.println(graph.traversal().V().has("label", "A").next().id());

        g.V().has("label", "A").next().edges(Direction.OUT)
                .forEachRemaining(edge -> System.out.println("EDGE: " + edge.inVertex()));
    }
}
