package com.mtanevski.master.lib;

import javafx.geometry.Point2D;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {

    public static Map<String, List<String>> getSampleGraph() {
        Map<String, List<String>> nodesAndConnections = new HashMap<>();
        nodesAndConnections.put("A", Arrays.asList("B", "F"));
        nodesAndConnections.put("B", Arrays.asList("C"));
        nodesAndConnections.put("C", Arrays.asList("D", "H"));
        nodesAndConnections.put("D", Arrays.asList("E"));
        nodesAndConnections.put("E", Arrays.asList("J"));
        nodesAndConnections.put("F", Arrays.asList("G", "K"));
        nodesAndConnections.put("G", Arrays.asList("L"));
        nodesAndConnections.put("H", Arrays.asList("I"));
        nodesAndConnections.put("I", Arrays.asList("N", "J"));
        nodesAndConnections.put("J", Arrays.asList("K"));
        nodesAndConnections.put("K", Arrays.asList("L", "P"));
        nodesAndConnections.put("L", Arrays.asList("M"));
        nodesAndConnections.put("M", Arrays.asList("R"));
        nodesAndConnections.put("N", Arrays.asList("S", "O"));
        nodesAndConnections.put("O", Arrays.asList("P"));
        nodesAndConnections.put("P", Arrays.asList("Q"));
        nodesAndConnections.put("Q", Arrays.asList("R"));
        nodesAndConnections.put("R", Arrays.asList("S"));
        nodesAndConnections.put("S", Arrays.asList("T"));
        nodesAndConnections.put("T", Arrays.asList());
        return nodesAndConnections;
    }

    public static Map<String, Point2D> nodesPositions() {
        Map<String, Point2D> nodesAndConnections = new HashMap<>();
        nodesAndConnections.put("A", new Point2D(50.0, 50.0));
        nodesAndConnections.put("B", new Point2D(100.0, 50.0));
        nodesAndConnections.put("C", new Point2D(150.0, 100.0));
        nodesAndConnections.put("D", new Point2D(200.0, 100.0));
        nodesAndConnections.put("E", new Point2D(100.0, 100.0));
        nodesAndConnections.put("F", new Point2D(50.0, 100.0));
        nodesAndConnections.put("G", new Point2D(100.0, 100.0));
        nodesAndConnections.put("H", new Point2D(100.0, 150.0));
        nodesAndConnections.put("I", new Point2D(100.0, 100.0));
        nodesAndConnections.put("J", new Point2D(100.0, 100.0));
        nodesAndConnections.put("K", new Point2D(100.0, 100.0));
        nodesAndConnections.put("L", new Point2D(100.0, 100.0));
        nodesAndConnections.put("M", new Point2D(100.0, 100.0));
        nodesAndConnections.put("N", new Point2D(100.0, 100.0));
        nodesAndConnections.put("O", new Point2D(100.0, 100.0));
        nodesAndConnections.put("P", new Point2D(100.0, 100.0));
        nodesAndConnections.put("Q", new Point2D(100.0, 100.0));
        nodesAndConnections.put("R", new Point2D(100.0, 100.0));
        nodesAndConnections.put("S", new Point2D(100.0, 100.0));
        nodesAndConnections.put("T", new Point2D(100.0, 100.0));
        return nodesAndConnections;
    }
}
