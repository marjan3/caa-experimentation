package com.mtanevski.master.lib.caa.impl.graph.tinker;

import com.mtanevski.master.lib.caa.CaaEdge;
import com.mtanevski.master.lib.caa.CaaGraph;
import com.mtanevski.master.lib.caa.impl.Constants;

import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.*;

public class TinkerCaaGraph implements CaaGraph {

    private static final String TRAVERSAL_WEIGHT = "traversalweight";
    private static final String WEIGHT = "weight";

    private final String graphFileLocation;
    private boolean directed;
    private GraphTraversalSource g;

    public TinkerCaaGraph(String graphFileLocation, boolean directed) {
        this.graphFileLocation = graphFileLocation;
        g = TinkerGraphUtils.open(this.graphFileLocation).traversal();
        this.directed = directed;
        this.validate();
        // added as a pre-caution step, to avoid having a corrupted weight data
        this.resetWeightData();
        this.resetTraversalWeightData();
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
    public List<CaaEdge> getAdjacentEdges(String vertex) {
        List<CaaEdge> adjacent = new ArrayList<>();
        Consumer<Edge> addEdge = e -> {
            adjacent.add(toCaaEdge(e, e.outVertex(), e.inVertex()));
        };
        this.g.V().has("label", vertex)
                .outE()
                .filter(edge -> edge.get().outVertex().value("label").equals(vertex))
                .forEachRemaining(addEdge);
        if (!this.directed) {
            Consumer<Edge> addEdgeOpposite = e -> {
                adjacent.add(toCaaEdge(e, e.inVertex(), e.outVertex()));
            };
            this.g.V().has("label", vertex)
                    .inE()
                    .filter(edge -> edge.get().inVertex().value("label").equals(vertex))
                    .forEachRemaining(addEdgeOpposite);
        }
        return adjacent.stream().distinct().collect(Collectors.toList());
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
    public CaaGraph clone() {
        // TODO: IMPROVE
        TinkerCaaGraph graph = this;
        return new CaaGraph() {
            @Override
            public List<String> getAllVertices() {
                return graph.getAllVertices();
            }

            @Override
            public List<CaaEdge> getAllEdges() {
                return graph.getAllEdges();
            }

            @Override
            public String getStart() {
                return graph.getStart();
            }

            @Override
            public List<String> getHappy() {
                return graph.getHappy();
            }

            @Override
            public List<String> getSad() {
                return graph.getSad();
            }

            @Override
            public boolean isVertexHappy(String vertex) {
                return graph.isVertexHappy(vertex);
            }

            @Override
            public boolean isVertexSad(String vertex) {
                return graph.isVertexSad(vertex);
            }

            @Override
            public List<String> getAdjacentVertices(String vertex) {
                return graph.getAdjacentVertices(vertex);
            }

            @Override
            public List<CaaEdge> getAdjacentEdges(String vertex) {
                return graph.getAdjacentEdges(vertex);
            }

            @Override
            public void incrementTraversalWeight(Double weight, String from, String to) {
                graph.incrementTraversalWeight(weight, from, to);
            }

            @Override
            public void incrementWeight(Double weight, String from, String to) {
                graph.incrementWeight(weight, from, to);
            }
            @Override
            public List<CaaEdge> getShortestPath(String from, String to) {
                return graph.getShortestPath(from, to);
            }

            @Override
            public Double getWeight(String from, String to) {
                return graph.getWeight(from, to);
            }

            @Override
            public CaaGraph clone() {
                return graph.clone();
            }

            @Override
            public void resetWeightData() {
                graph.resetWeightData();
            }

            @Override
            public boolean hasHappiness(String from, String to) {
                return graph.hasHappiness(from, to);
            }

            @Override
            public boolean hasSadness(String from, String to) {
                return graph.hasSadness(from, to);
            }

            @Override
            public void resetTraversalWeightData() {
                graph.resetTraversalWeightData();
            }

        };
    }

    @Override
    public void resetWeightData() {
        g.E().forEachRemaining(edge -> {
            edge.property(WEIGHT, Constants.EDGES_WEIGHT);
        });
    }

    @Override
    public void resetTraversalWeightData() {
        g.E().forEachRemaining(edge -> {
            edge.property(TRAVERSAL_WEIGHT, Constants.EDGES_TRAVERSAL_WEIGHT);
        });
    }

    @Override
    public List<CaaEdge> getShortestPath(String from, String to) {
        GraphTraversal<Vertex, Vertex> repeatTraversal = this.directed ? out().simplePath() : both().simplePath();
        GraphTraversal<Vertex, Path> path = g.V().has("label", from).repeat(repeatTraversal).until(has("label", to)).path().by("label").limit(1);
        Path p = path.toList().get(0);
        List<CaaEdge> caaEdges = new ArrayList<>();
        for (int i = 0; i < (p.size() - 1); i++) {
            caaEdges.add(new TinkerCaaEdge(p.objects().get(i).toString(), p.objects().get(i + 1).toString(), null, null));
        }
        return caaEdges;
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

    private TinkerCaaEdge toCaaEdge(Edge e, Vertex vertex, Vertex vertex2) {
        return new TinkerCaaEdge(vertex.value("label"), vertex2.value("label"), e.<Double>value(WEIGHT), e.<Double>value(TRAVERSAL_WEIGHT));
    }

}
