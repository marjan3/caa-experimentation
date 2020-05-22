package com.mtanevski.master.caa.gui.web;

import com.google.gson.JsonObject;
import com.mtanevski.master.caa.gui.DefaultValues;
import com.mtanevski.master.caa.gui.models.UserPreferences;
import javafx.scene.paint.Color;


public class VisOptionsMapper {

    /**
     * @return {
     * nodes: {
     * size: 42,
     * font: {
     * color: "#111",
     * face: "Arvo",
     * size: 48,
     * strokeWidth: 1,
     * strokeColor: "#222"
     * }
     * },
     * edges: {
     * font: {
     * color: "#111",
     * face: "Arvo",
     * size: 0
     * },
     * color: {
     * color: "#CCC",
     * highlight: "#A22"
     * },
     * length: 275
     * }
     * }
     */
    public static String toJson(UserPreferences preferences) {
        JsonObject root = new JsonObject();
        JsonObject edges = new JsonObject();
        JsonObject nodes = new JsonObject();
        root.addProperty("happyNodesColor", WebUtils.toWeb(preferences.getHappyVertexColor()));
        root.addProperty("sadNodesColor", WebUtils.toWeb(preferences.getSadVertexColor()));
        root.addProperty("startNodeColor", WebUtils.toWeb(preferences.getStartingVertexColor()));

        nodes.addProperty("size", DefaultValues.NODES_SIZE);
        nodes.addProperty("color", WebUtils.toWeb(preferences.getVerticesColor()));
        JsonObject nodesFont = new JsonObject();
        nodesFont.addProperty("color", WebUtils.toWeb(Color.BLACK));
        nodesFont.addProperty("face", "Arvo");
        nodesFont.addProperty("size", WebUtils.toNodeFontSize(preferences.getShowVerticesLabels()));
        nodesFont.addProperty("strokeWidth", DefaultValues.NODES_FONT_STROKE_WIDTH);
        nodesFont.addProperty("strokeColor", DefaultValues.NODES_FONT_STROKE_COLOR);
        nodes.add("font", nodesFont);
        root.add("nodes", nodes);


        edges.addProperty("length", DefaultValues.EDGES_LENGTH);
        edges.addProperty("width", preferences.getEdgesWidth());
        JsonObject edgesFont = new JsonObject();
        edgesFont.addProperty("color", WebUtils.toWeb(preferences.getEdgesColor()));
        edgesFont.addProperty("face", "Arvo");
        edgesFont.addProperty("size", WebUtils.toEdgeFontSize(preferences.getShowEdgesLabels()));
        edges.add("font", edgesFont);
        JsonObject edgesBackground = new JsonObject();
        edgesBackground.addProperty("color", WebUtils.toWeb(preferences.getShortestPathColor()));
        edges.add("background", edgesBackground);
        JsonObject edgesColor = new JsonObject();
        edgesColor.addProperty("color", WebUtils.toWeb(preferences.getEdgesColor()));
        edgesColor.addProperty("highlight", "#A22");
        edges.add("color", edgesColor);
        root.add("edges", edges);

        return root.toString();
    }
}
