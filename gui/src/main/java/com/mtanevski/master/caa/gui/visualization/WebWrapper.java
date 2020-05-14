package com.mtanevski.master.caa.gui.visualization;

import com.google.gson.Gson;
import com.mtanevski.master.caa.gui.CaaExperimentSaveData;
import com.mtanevski.master.caa.gui.DefaultValues;
import com.mtanevski.master.caa.gui.SessionManager;
import com.mtanevski.master.caa.gui.alerts.ErrorAlert;
import com.mtanevski.master.caa.gui.preferences.PreferencesConstants;
import com.mtanevski.master.caa.gui.preferences.PreferencesController;
import com.mtanevski.master.caa.gui.visualization.js.JSFunctions;
import com.mtanevski.master.caa.gui.visualization.js.JSMembers;
import com.mtanevski.master.caa.lib.CaaEdge;
import com.mtanevski.master.caa.lib.CaaExperimentResults;
import com.mtanevski.master.caa.lib.CaaExperimenter;
import com.mtanevski.master.caa.lib.CaaGraph;
import com.mtanevski.master.caa.lib.impl.agents.CaaAgentFactory;
import com.mtanevski.master.caa.lib.impl.agents.CaaAgentType;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

import static javafx.concurrent.Worker.State.SUCCEEDED;

public class WebWrapper {

    private static final Preferences p = Preferences.userNodeForPackage(PreferencesController.class);
    private final Gson gson;
    private final WebView web;
    private JSObject window;
    private VisData visData;
    public WebWrapper(WebView web) {
        try {
            p.clear();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
        gson = new Gson();
        String html = Objects.requireNonNull(WebWrapper.class.getClassLoader()
                .getResource("visualization/index.html"))
                .toExternalForm();
        this.web = web;
        WebEngine webEngine = this.web.getEngine();
        webEngine.load(html);

        // set up the listener
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (SUCCEEDED == newValue) {
                // set an interface object named 'javaConnector' in the visualization engine's page
                window = (JSObject) webEngine.executeScript("window");

                this.visualizeGraph(SessionManager.graph());
                // window.setMember("javaConnector", javaConnector);
                // get the Javascript connector object.
                // javascriptConnector = (JSObject) webEngine.executeScript("getJsConnector()");
            }
        });
//        WebConsoleListener.setDefaultListener((webView, message, lineNumber, sourceId) ->
//                System.out.println(message + "[at " + lineNumber + "]"));
        webEngine.setOnAlert(eventData -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(DefaultValues.GRAPH_VIS_TITLE);
            alert.setHeaderText(null);
            alert.setContentText(eventData.getData());

            alert.showAndWait();
        });
    }

    public WebView getWeb() {
        return web;
    }

    public void visualizeGraph(CaaGraph graph) {

        try {
            List<String[]> edgesData =
                    graph.getAllEdges().stream()
                            .map(e -> new String[]{e.getFrom(), e.getTo()})
                            .collect(Collectors.toList());
            visData = new VisData(graph.getAllVertices(), edgesData, graph.getStart(), graph.getHappy(), graph.getSad());
            String json = visData.toJson();
            window.call(JSFunctions.setVisData.name(), json);
            String options = new VisOptions().toJson();
            window.call(JSFunctions.setVisOptions.name(), options);
            this.setStartVisNodeColor(Color.valueOf(p.get(PreferencesConstants.STARTING_VERTEX_COLOR, DefaultValues.STARTING_VERTEX_COLOR)));
            this.setHappyVisNodesColor(Color.valueOf(p.get(PreferencesConstants.HAPPY_VERTEX_COLOR, DefaultValues.HAPPY_VERTEX_COLOR)));
            this.setSadVisNodesColor(Color.valueOf(p.get(PreferencesConstants.SAD_VERTEX_COLOR, DefaultValues.SAD_VERTEX_COLOR)));
        } catch (Exception exception) {
            ErrorAlert errorAlert = new ErrorAlert("Can not visualize graph", "Invalid graph provided", exception.getMessage());
            errorAlert.showAndWait();
            throw new IllegalStateException(exception);
        }
    }

    public void visualizeCaaAlgorithmTraversal(CaaAgentType caaAgentType, Boolean animate) {
        Iterator<CaaEdge> iterator = SessionManager.graph().iterator(CaaAgentFactory.getAgent(caaAgentType));
        CaaExperimentResults results = new CaaExperimentResults(SessionManager.graph().getStart());
        while (iterator.hasNext()) {
            CaaExperimenter.trailEdge(iterator.next(), results);
        }
        if (animate) {
            window.call(JSFunctions.animateCaaVertices.name(), gson.toJson(results.current().getTraversedVertices()));
        } else {
            window.call(JSFunctions.drawCaaVertices.name(), gson.toJson(results.current().getTraversedVertices()));
        }
    }

    public void visualizeCaaExperiment(Double happyMultiplier, Double sadMultiplier, CaaAgentType caaAgentType, Boolean animate) {
        SessionManager.graph().resetWeightData();

        CaaExperimentSaveData experimentData = SessionManager.newExperiment(happyMultiplier, sadMultiplier, caaAgentType);

        String json = gson.toJson(experimentData.getResults().getHistory());
        String happyColor = VisOptions.toWeb(p.get(PreferencesConstants.HAPPY_VERTEX_COLOR, DefaultValues.HAPPY_VERTEX_COLOR));
        String sadColor = VisOptions.toWeb(p.get(PreferencesConstants.SAD_VERTEX_COLOR, DefaultValues.SAD_VERTEX_COLOR));

        // How to animate browser from Java:
        //
        //   AtomicInteger runCount = new AtomicInteger();
        //   final Timeline incrementsInterval = new Timeline(new KeyFrame(Duration.seconds(1), event ->
        //   {
        //       int i = runCount.getAndIncrement();
        //       String j = gson.toJson(caaExperimenter.getTraversalHistory().get(i).getIncrements());
        //       window.call("animateCaaIncrements", j, happyColor, sadColor);
        //   }));
        //
        //   incrementsInterval.setCycleCount(caaExperimenter.getTraversalHistory().size());
        //   incrementsInterval.play();
        //   incrementsInterval.setOnFinished((eventData) -> {
        //       Alert alert = new Alert(Alert.AlertType.INFORMATION);
        //       alert.setTitle("Graph Visualization");
        //       alert.setHeaderText(null);
        //       alert.setContentText("");
        //
        //       alert.show();
        //   });

        if (animate) {
            window.call(JSFunctions.animateCaaExperiment.name(), json, happyColor, sadColor);
        } else {
            window.call(JSFunctions.drawCaaExperiment.name(), json, happyColor, sadColor);
        }
    }

    public void markShortestPaths() {
        setVisEdgesBackground(true);
    }

    public void removeShortestPaths() {
        setVisEdgesBackground(false);
    }

    public void setHappyVisNodesColor(Color value) {
        window.call(JSFunctions.setVisNodesColor.name(), new Gson().toJson(visData.getHappyNodes()), VisOptions.toWeb(value));
    }

    public void setSadVisNodesColor(Color value) {
        window.call(JSFunctions.setVisNodesColor.name(), gson.toJson(visData.getSadNodes()), VisOptions.toWeb(value));
    }

    public void setStartVisNodeColor(Color value) {
        window.call(JSFunctions.setVisNodesColor.name(), gson.toJson(Collections.singletonList(visData.getStartNode())), VisOptions.toWeb(value));
    }

    private void setVisEdgesBackground(boolean enabled) {
        String startVertex = SessionManager.graph().getStart();
        for (String vertex : SessionManager.graph().getHappy()) {
            List<CaaEdge> shortestPath = SessionManager.graph().getShortestPath(startVertex, vertex);
            window.call(JSFunctions.setVisEdgesBackground.name(), gson.toJson(shortestPath), enabled);
        }
    }

    public <T> void setOption(String path, T value) {
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
