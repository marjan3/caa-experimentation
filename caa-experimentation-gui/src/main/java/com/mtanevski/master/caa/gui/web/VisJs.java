package com.mtanevski.master.caa.gui.web;

import com.google.gson.Gson;
import com.mtanevski.master.caa.gui.models.UserPreferences;
import com.mtanevski.master.caa.gui.web.visjs.JSFunctions;
import com.mtanevski.master.caa.gui.web.visjs.JSMembers;
import com.mtanevski.master.caa.gui.web.visjs.VisData;
import com.mtanevski.master.caa.lib.CaaEdge;
import com.mtanevski.master.caa.lib.CaaExperimentResults;
import com.mtanevski.master.caa.lib.CaaGraph;
import javafx.scene.paint.Color;
import netscape.javascript.JSObject;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class VisJs {

    private final JSObject window;
    private final Gson gson;
    private VisData visData;

    public VisJs(JSObject window) {
        this.window = window;
        this.gson = new Gson();
    }

    public void visualizeGraph(CaaGraph graph) {
        List<String[]> edgesData =
                graph.getAllEdges().stream()
                        .map(e -> new String[]{e.getFrom(), e.getTo()})
                        .collect(Collectors.toList());
        this.visData = new VisData(graph.getAllVertices(), edgesData, graph.getStart(), graph.getHappy(), graph.getSad());
        String json = visData.toJson();
        window.call(JSFunctions.setVisData.name(), json);
    }

    public void setOptions(UserPreferences userPreferences) {
        String visOptions = VisOptionsMapper.toJson(userPreferences);
        window.call(JSFunctions.setVisOptions.name(), visOptions);
        String caaOptions = CaaOptionsMapper.toJson(userPreferences);
        window.call(JSFunctions.setCaaOptions.name(), caaOptions);
    }

    public void animateCaaVertices(CaaExperimentResults results, boolean animate) {
        if (animate) {
            window.call(JSFunctions.animateCaaVertices.name(), gson.toJson(results.current().getTraversedVertices()));
        } else {
            window.call(JSFunctions.drawCaaVertices.name(), gson.toJson(results.current().getTraversedVertices()));
        }
    }

    public void animateCaaExperiment(CaaExperimentResults results, boolean animate) {
        String json = gson.toJson(results.getHistory());

        if (animate) {
            window.call(JSFunctions.animateCaaExperiment.name(), json);
        } else {
            window.call(JSFunctions.drawCaaExperiment.name(), json);
        }
    }

    public void visualizeShortestPaths(CaaGraph caaGraph) {
        setVisEdgesBackground(true, caaGraph);
    }

    public void removeVisualizationForShortestPaths(CaaGraph caaGraph) {
        setVisEdgesBackground(false, caaGraph);
    }

    public void setHappyVisNodesColor(Color value) {
        window.call(JSFunctions.setVisNodesColor.name(), gson.toJson(visData.getHappyNodes()), WebUtils.toWeb(value));
    }

    public void setSadVisNodesColor(Color value) {
        window.call(JSFunctions.setVisNodesColor.name(), gson.toJson(visData.getSadNodes()), WebUtils.toWeb(value));
    }

    public void setStartVisNodeColor(Color value) {
        window.call(JSFunctions.setVisNodesColor.name(), gson.toJson(Collections.singletonList(visData.getStartNode())), WebUtils.toWeb(value));
    }

    private void setVisEdgesBackground(boolean enabled, CaaGraph graph) {
        String startVertex = graph.getStart();
        for (String vertex : graph.getHappy()) {
            List<CaaEdge> shortestPath = graph.getShortestPath(startVertex, vertex);
            window.call(JSFunctions.setVisEdgesBackground.name(), gson.toJson(shortestPath), enabled);
        }
    }

    public void setVerticesColor(Color color) {
        this.setOption("nodes.color", WebUtils.toWeb(color));
    }

    public void setEdgesColor(Color color) {
        this.setOption("edges.color.color", WebUtils.toWeb(color));
    }

    public void setShowVerticesLabels(Boolean value) {
        this.setOption("nodes.font.size", WebUtils.toNodeFontSize(value));
    }

    public void setShowEdgesLabels(Boolean value) {
        this.setOption("edges.font.size", WebUtils.toEdgeFontSize(value));
    }

    public void setEdgesWidth(Number value) {
        this.setOption("edges.width", value);
    }

    public void setShortestPathColor(Color value) {
        this.setOption("edges.background.color", WebUtils.toWeb(value));
    }

    private <T> void setOption(String path, T value) {
        String[] members = path.split("\\.");
        if (members.length <= 0) {
            throw new IllegalArgumentException("Invalid path");
        }

        JSObject visOptions = (JSObject) window.getMember(JSMembers.visOptions.name());
        JSObject parent = (JSObject) visOptions.getMember(members[0]);
        String lastPath = members[members.length - 1];
        if (members.length > 1) {
            for (int i = 1; i < members.length - 1; i++) {
                String subMember = members[i];
                parent = (JSObject) parent.getMember(subMember);
            }
        }

        parent.setMember(lastPath, value);
        window.call(JSFunctions.setVisOptions.name());
    }
}
