package com.mtanevski.master.lib.caa.impl.graph.tinker;

import org.apache.tinkerpop.gremlin.process.traversal.IO;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

public class TinkerGraphUtils {

    public static Graph open(String location) {
        Graph graph = TinkerGraph.open();
        TinkerGraph.open();
        graph.traversal()
                .io(location)
                .with(IO.reader, IO.graphml)
                .read()
                .iterate();
        return graph;

    }
}
