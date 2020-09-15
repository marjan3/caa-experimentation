package com.mtanevski.master.caa.lib.impl.graphs.tinker;

import com.mtanevski.master.caa.lib.CaaEdge;
import com.mtanevski.master.caa.lib.CaaGraph;
import com.mtanevski.master.caa.lib.Constants;
import com.mtanevski.master.caa.lib.impl.graphs.readonly.ImmutableCaaGraph;
import org.apache.tinkerpop.gremlin.process.traversal.IO;
import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.both;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.has;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.out;

public class TinkerCaaGraph implements CaaGraph {

    private static final String TRAVERSAL_WEIGHT = "traversalweight";
    private static final String WEIGHT = "weight";

    private final String graphFileLocation;
    private final boolean directed;
    private final GraphTraversalSource g;

    public TinkerCaaGraph(String graphFileLocation, boolean directed) {
        this.graphFileLocation = graphFileLocation;
        g = TinkerCaaGraph.open(this.graphFileLocation).traversal();
        this.directed = directed;
        this.validate();
        // added as a pre-caution step, to avoid having a corrupted weight data
        this.resetWeightData();
        this.resetTraversalWeightData();
    }

    private static Graph open(String location) {
        Graph graph = TinkerGraph.open();
        TinkerGraph.open();
        graph.traversal()
                .io(location)
                .with(IO.reader, IO.graphml)
                .read()
                .iterate();
        return graph;
    }

    @Override
    public List<String> getAllVertices() {
        return g.V().toList().stream().map(vertex -> vertex.<String>value("label")).collect(Collectors.toList());
    }

    @Override
    public List<CaaEdge> getAllEdges() {
        return g.E().toList().stream()
                .map(e -> toCaaEdge(e, e.inVertex(), e.outVertex()))
                .collect(Collectors.toList());
    }

    @Override
    public String getStart() {
        return g.V()
                .has("type", "start")
                .next()
                .value("label");
    }

    @Override
    public List<String> getHappy() {
        return g.V()
                .has("type", "happy")
                .map(vertex -> vertex.get().<String>value("label")).toList();
    }

    @Override
    public List<String> getSad() {
        return g.V()
                .has("type", "sad")
                .map(vertex -> vertex.get().<String>value("label")).toList();
    }

    @Override
    public boolean isVertexHappy(String vertex) {
        return g.V()
                .has("label", vertex)
                .has("type", "happy")
                .hasNext();
    }

    @Override
    public boolean isVertexSad(String vertex) {
        return g.V()
                .has("label", vertex)
                .has("type", "sad")
                .hasNext();
    }

    @Override
    public List<String> getAdjacentVertices(String vertex) {
        return g.V()
                .has("label", vertex)
                .bothE()
                .otherV().<String>map(e -> e.get().value("label"))
                .toList();
    }

    @Override
    public void incrementWeight(Double weight, String fromVertexLabel, String toVertexLabel) {
        Consumer<Edge> edgeUpdate = edge -> edge.property(WEIGHT, edge.<Double>value(WEIGHT) + weight);
        changeEdgeProperty(edgeUpdate, fromVertexLabel, toVertexLabel);
    }

    @Override
    public void incrementTraversalWeight(Double weight, String from, String to) {
        Consumer<Edge> edgeUpdate = edge -> edge.property(TRAVERSAL_WEIGHT, edge.<Double>value(TRAVERSAL_WEIGHT) + weight);
        this.changeEdgeProperty(edgeUpdate, from, to);
    }

    @Override
    public Double getWeight(String fromVertexLabel, String toVertexLabel) {
        List<Edge> directedEdges = g.V()
                .has("label", fromVertexLabel)
                .outE()
                .filter(edge -> edge.get().inVertex().value("label").equals(toVertexLabel))
                .toList();
        if (directedEdges.size() > 0) {
            return directedEdges.get(0).value(WEIGHT);
        }
        if (!this.directed) {
            List<Edge> otherDirectionEdges = g.V()
                    .has("label", toVertexLabel)
                    .outE()
                    .filter(edge -> edge.get().inVertex().value("label").equals(fromVertexLabel))
                    .toList();
            if (otherDirectionEdges.size() > 0) {
                return otherDirectionEdges.get(0).value(WEIGHT);
            }
        }

        throw new IllegalArgumentException("Getting weight failed due to edge not found for " + fromVertexLabel + "->" + toVertexLabel);
    }

    @Override
    public boolean hasHappiness(String from, String to) {
        return this.getWeight(from, to) > 0.0;
    }

    @Override
    public boolean hasSadness(String from, String to) {
        return this.getWeight(from, to) < 0.0;
    }

    @Override
    public List<CaaEdge> getAdjacentEdges(String vertex) {
        List<CaaEdge> adjacent = new ArrayList<>();
        Consumer<Edge> addEdge = e -> adjacent.add(toCaaEdge(e, e.outVertex(), e.inVertex()));
        this.g.V().has("label", vertex)
                .outE()
                .filter(edge -> edge.get().outVertex().value("label").equals(vertex))
                .forEachRemaining(addEdge);
        if (!this.directed) {
            Consumer<Edge> addEdgeOpposite = e -> adjacent.add(toCaaEdge(e, e.inVertex(), e.outVertex()));
            this.g.V().has("label", vertex)
                    .inE()
                    .filter(edge -> edge.get().inVertex().value("label").equals(vertex))
                    .forEachRemaining(addEdgeOpposite);
        }
        return adjacent.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public CaaGraph immutableClone() {
        return new ImmutableCaaGraph(this);
    }

    @Override
    public void resetWeightData() {
        g.E().forEachRemaining(edge -> edge.property(WEIGHT, Constants.EDGES_WEIGHT));
    }

    @Override
    public void resetTraversalWeightData() {
        g.E().forEachRemaining(edge -> edge.property(TRAVERSAL_WEIGHT, Constants.EDGES_TRAVERSAL_WEIGHT));
    }

    @Override
    public String toString() {
        return "start='" + this.getStart() + "'" + "\n" +
                "happy='" + this.getHappy() + "'" + "\n" +
                "sad='" + this.getSad() + "'" + "\n" +
                "vertices='" + this.getAllVertices() + '\'' + "\n" +
                "edges=" + this.getAllEdges() + "\n";
    }

    private void validate() {
        List<Vertex> startVertices = g.V().has("type", "start").toList();
        if (startVertices.isEmpty()) {
            throw new IllegalArgumentException("Graph does not have a starting vertex");
        }
        if (startVertices.size() > 1) {
            throw new IllegalArgumentException("Graph has more than one starting vertices");
        }
        List<Vertex> happyVertices = g.V().has("type", "happy").toList();
        if (happyVertices.isEmpty()) {
            throw new IllegalArgumentException("Graph does not have happy vertices");
        }
    }

    private void changeEdgeProperty(Consumer<Edge> incrementEdge, String fromVertexLabel, String toVertexLabel) {
        g.V()
                .has("label", fromVertexLabel)
                .outE()
                .filter(edge -> edge.get().inVertex().value("label").equals(toVertexLabel))
                .forEachRemaining(incrementEdge);
        if (!this.directed && !fromVertexLabel.equals(toVertexLabel)) {
            g.V()
                    .has("label", toVertexLabel)
                    .outE()
                    .filter(edge -> edge.get().inVertex().value("label").equals(fromVertexLabel))
                    .forEachRemaining(incrementEdge);
        }
    }

    @Override
    public List<CaaEdge> getShortestPath(String from, String to) {
        GraphTraversal<Vertex, Vertex> repeatTraversal = this.directed ? out().simplePath() : both().simplePath();
        // skipping sad vertices
        this.getSad().forEach(v -> repeatTraversal.not(has("label", v)));

        GraphTraversal<Vertex, Path> path = g.V()
                .has("label", from)
                .repeat(repeatTraversal)
                .until(has("label", to))
                .path()
                .by("label").limit(1);
        Path p = path.toList().get(0);
        List<CaaEdge> caaEdges = new ArrayList<>();
        for (int i = 0; i < (p.size() - 1); i++) {
            caaEdges.add(new CaaEdge(p.objects().get(i).toString(), p.objects().get(i + 1).toString(), 0, 0));
        }
        return caaEdges;
    }

    private CaaEdge toCaaEdge(Edge e, Vertex vertex, Vertex vertex2) {
        return new CaaEdge(vertex.value("label"), vertex2.value("label"), e.<Double>value(WEIGHT), e.<Double>value(TRAVERSAL_WEIGHT));
    }

}
