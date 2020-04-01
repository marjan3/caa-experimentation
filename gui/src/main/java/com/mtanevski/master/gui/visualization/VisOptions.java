package com.mtanevski.master.gui.visualization;

import com.google.gson.JsonObject;
import com.mtanevski.master.gui.DefaultValues;
import com.mtanevski.master.gui.preferences.PreferencesConstants;
import com.mtanevski.master.gui.preferences.PreferencesController;
import javafx.scene.paint.Color;

import java.util.prefs.Preferences;

import static com.mtanevski.master.gui.DefaultValues.NODES_FONT_SIZE;


public class VisOptions {

    private static Preferences p = Preferences.userNodeForPackage(PreferencesController.class);

    JsonObject root = new JsonObject();
    JsonObject edges = new JsonObject();
    JsonObject nodes = new JsonObject();

    public static int toNodeFontSize(Boolean n) {
        return Boolean.valueOf(n.toString()) ? NODES_FONT_SIZE : 0;
    }

    public static int toEdgeFontSize(Boolean n) {
        return Boolean.valueOf(n.toString()) ? DefaultValues.EDGES_FONT_SIZE : 0;
    }

    public static String toWeb(Color color) {
        return color.toString().replace("0x", "#");
    }

    public static String toWeb(String color) {
        return color.replace("0x", "#");
    }

    /**
     * {
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
     *
     * @return
     */
    public String toJson() {

        nodes.addProperty("size", DefaultValues.NODES_SIZE);
        nodes.addProperty("color", toWeb(Color.valueOf(p.get(PreferencesConstants.VERTICES_COLOR, DefaultValues.VERTICES_COLOR))));
        JsonObject nodesFont = new JsonObject();
        nodesFont.addProperty("color", toWeb(Color.BLACK));
        nodesFont.addProperty("face", "Arvo");
        nodesFont.addProperty("size", toNodeFontSize(p.getBoolean(PreferencesConstants.VERTICES_LABELS, DefaultValues.VERTICES_LABELS)));
        nodesFont.addProperty("strokeWidth", DefaultValues.NODES_FONT_STROKE_WIDTH);
        nodesFont.addProperty("strokeColor", DefaultValues.NODES_FONT_STROKE_COLOR);
        nodes.add("font", nodesFont);
        root.add("nodes", nodes);


        edges.addProperty("length", DefaultValues.EDGES_LENGTH);
        edges.addProperty("width", p.getDouble(PreferencesConstants.EDGES_WIDTH, DefaultValues.EDGES_WIDTH));
        JsonObject edgesFont = new JsonObject();
        edgesFont.addProperty("color", toWeb(Color.valueOf(p.get(PreferencesConstants.EDGES_COLOR, DefaultValues.EDGES_COLOR))));
        edgesFont.addProperty("face", "Arvo");
        edgesFont.addProperty("size", toEdgeFontSize(p.getBoolean(PreferencesConstants.EDGES_LABELS, DefaultValues.EDGES_LABELS)));
        edges.add("font", edgesFont);
        JsonObject edgesBackground = new JsonObject();
        edgesBackground.addProperty("color", toWeb(Color.valueOf(p.get(PreferencesConstants.SHORTEST_PATH_COLOR, DefaultValues.SHORTEST_PATH_COLOR))));
        edges.add("background", edgesBackground);
        JsonObject edgesColor = new JsonObject();
        edgesColor.addProperty("color", toWeb(Color.valueOf(p.get(PreferencesConstants.EDGES_COLOR, DefaultValues.EDGES_COLOR))));
        edgesColor.addProperty("highlight", "#A22");
        edges.add("color", edgesColor);
        root.add("edges", edges);

        return root.toString();
    }
}
