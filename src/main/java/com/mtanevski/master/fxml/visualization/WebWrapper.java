package com.mtanevski.master.fxml.visualization;

import com.google.gson.Gson;
import com.mtanevski.master.fxml.DefaultValues;
import com.mtanevski.master.fxml.alerts.ErrorAlert;
import com.mtanevski.master.fxml.preferences.PreferencesConstants;
import com.mtanevski.master.fxml.preferences.PreferencesController;
import com.mtanevski.master.fxml.utils.SessionManager;
import com.mtanevski.master.fxml.visualization.js.JSMembers;
import com.mtanevski.master.lib.caa.CaaEdge;
import com.mtanevski.master.lib.caa.CaaExperimenter;
import com.mtanevski.master.lib.caa.CaaGraph;
import com.mtanevski.master.lib.caa.impl.agents.CaaAgentFactory;
import com.sun.javafx.webkit.WebConsoleListener;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.util.Collections;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

import static com.mtanevski.master.fxml.DefaultValues.GRAPH_VIS_TITLE;
import static com.mtanevski.master.fxml.visualization.VisOptions.toWeb;
import static com.mtanevski.master.fxml.visualization.js.JSFunctions.*;
import static javafx.concurrent.Worker.State.SUCCEEDED;

public class WebWrapper {

    private static Preferences p = Preferences.userNodeForPackage(PreferencesController.class);
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
        String html = WebWrapper.class.getClassLoader().getResource("visualization/index.html").toExternalForm();
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
        WebConsoleListener.setDefaultListener((webView, message, lineNumber, sourceId) -> {
            System.out.println(message + "[at " + lineNumber + "]");
        });
        webEngine.setOnAlert(eventData -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(GRAPH_VIS_TITLE);
            alert.setHeaderText(null);
            alert.setContentText(eventData.getData());

            alert.showAndWait();
        });
    }

    public void visualizeGraph(CaaGraph graph) {

        try {
            List<String[]> edgesData =
                    graph.getAllEdges().stream()
                            .map(e -> new String[]{e.getFrom(), e.getTo()})
                            .collect(Collectors.toList());
            visData = new VisData(graph.getAllVertices(), edgesData, graph.getStart(), graph.getHappy(), graph.getSad());
            String json = visData.toJson();
            window.call(setVisData.name(), json);
            String options = new VisOptions().toJson();
            window.call(setVisOptions.name(), options);
            this.setStartVisNodeColor(Color.valueOf(p.get(PreferencesConstants.STARTING_VERTEX_COLOR, DefaultValues.STARTING_VERTEX_COLOR)));
            this.setHappyVisNodesColor(Color.valueOf(p.get(PreferencesConstants.HAPPY_VERTEX_COLOR, DefaultValues.HAPPY_VERTEX_COLOR)));
            this.setSadVisNodesColor(Color.valueOf(p.get(PreferencesConstants.SAD_VERTEX_COLOR, DefaultValues.SAD_VERTEX_COLOR)));
        }catch (Exception exception){
            ErrorAlert errorAlert = new ErrorAlert("Can not visualize graph", "Invalid graph provided", exception.getMessage());
            errorAlert.showAndWait();
            throw new IllegalStateException(exception);
        }
    }

    public void visualizeCaaAlgorithmTraversal(CaaAgentFactory.CaaAgentType caaAgentType, Boolean animate) {
        CaaExperimenter experimenter = SessionManager.experimenter();
        experimenter.executeTraversal(CaaAgentFactory.getAgent(caaAgentType));
        if(animate) {
            window.call(animateCaaVertices.name(), gson.toJson(experimenter.getData().current().traversedVertices()));
        } else {
            window.call(drawCaaVertices.name(), gson.toJson(experimenter.getData().current().traversedVertices()));
        }
    }

    public void visualizeCaaExperiment(Double happyMultiplier, Double sadMultiplier, CaaAgentFactory.CaaAgentType caaAgentType, Boolean animate) {
        SessionManager.graph().resetWeightData();
        CaaExperimenter experimenter = SessionManager.newExperimenter(happyMultiplier, sadMultiplier);
        experimenter.executeExperiment(CaaAgentFactory.getAgent(caaAgentType));
        String json = gson.toJson(experimenter.getData().getHistory());
        String happyColor = toWeb(p.get(PreferencesConstants.HAPPY_VERTEX_COLOR, DefaultValues.HAPPY_VERTEX_COLOR));
        String sadColor = toWeb(p.get(PreferencesConstants.SAD_VERTEX_COLOR, DefaultValues.SAD_VERTEX_COLOR));

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

        if(animate) {
            window.call(animateCaaExperiment.name(), json, happyColor, sadColor);
        } else {
            window.call(drawCaaExperiment.name(), json, happyColor, sadColor);
        }
    }

    public void markShortestPaths() {
        setVisEdgesBackground(true);
    }

    public void removeShortestPaths() {
        setVisEdgesBackground(false);
    }

    public void setHappyVisNodesColor(Color value) {
        window.call(setVisNodesColor.name(), new Gson().toJson(visData.getHappyNodes()), toWeb(value));
    }

    public void setSadVisNodesColor(Color value) {
        window.call(setVisNodesColor.name(), gson.toJson(visData.getSadNodes()), toWeb(value));
    }

    public void setStartVisNodeColor(Color value) {
        window.call(setVisNodesColor.name(), gson.toJson(Collections.singletonList(visData.getStartNode())), toWeb(value));
    }

    private void setVisEdgesBackground(boolean enabled) {
        String startVertex = SessionManager.graph().getStart();
        for (String vertex : SessionManager.graph().getHappy()) {
            List<CaaEdge> shortestPath = SessionManager.graph().getShortestPath(startVertex, vertex);
            window.call(setVisEdgesBackground.name(), gson.toJson(shortestPath), enabled);
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
        window.call(setVisOptions.name());
    }

}
