package com.mtanevski.master.caa.gui.visualization;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class VisData {

    private List<VisNode> nodes = new ArrayList<>();
    private List<VisEdge> edges = new ArrayList<>();

    private Integer startNode;
    private List<Integer> happyNodes = new ArrayList<>();
    private List<Integer> sadNodes = new ArrayList<>();

    public VisData(List<String> nodesData, List<String[]> edgesData, String startNode, List<String> happyNodes, List<String> sadNodes) {
        for (int i = 0; i < nodesData.size(); i++) {
            this.nodes.add(new VisNode(i, nodesData.get(i)));
        }

        for (int i = 0; i < edgesData.size(); i++) {
            String[] e = edgesData.get(i);
            this.edges.add(new VisEdge(i, getNode(e[0]), getNode(e[1])));
        }

        this.setStartNode(toStartNode(startNode));
        this.setHappyNodes(toHappyNodes(happyNodes));
        this.setSadNodes(toSadNodes(sadNodes));
    }

    public VisNode getNode(String label) {
        for (VisNode node : this.nodes) {
            if (label.equals(node.getLabel())) {
                return node;
            }
        }
        throw new IllegalArgumentException("Node with label " + label + " not found");
    }

    public List<VisNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<VisNode> nodes) {
        this.nodes = nodes;
    }

    public List<VisEdge> getEdges() {
        return edges;
    }

    public void setEdges(List<VisEdge> edges) {
        this.edges = edges;
    }

    public Integer getStartNode() {
        return startNode;
    }

    public void setStartNode(int startNode) {
        this.startNode = startNode;
    }


    public int toStartNode(String startNode) {
        Objects.requireNonNull(startNode);
        return this.getNode(startNode).getId();
    }

    public List<Integer> getHappyNodes() {
        return happyNodes;
    }

    public void setHappyNodes(List<Integer> happyNodes) {
        this.happyNodes = happyNodes;
    }

    private List<Integer> toHappyNodes(List<String> happyNodes) {
        Objects.requireNonNull(happyNodes);
        return happyNodes.stream()
                .map(node -> getNode(node).getId())
                .collect(Collectors.toList());
    }

    public List<Integer> getSadNodes() {
        return sadNodes;
    }

    public void setSadNodes(List<Integer> sadNodes) {
        this.sadNodes = sadNodes;
    }

    private List<Integer> toSadNodes(List<String> sadNodes) {
        Objects.requireNonNull(sadNodes);
        return sadNodes.stream()
                .map(node -> getNode(node).getId())
                .collect(Collectors.toList());
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
