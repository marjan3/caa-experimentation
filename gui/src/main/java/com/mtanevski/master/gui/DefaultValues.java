package com.mtanevski.master.gui;

import com.mtanevski.master.lib.caa.impl.agents.CaaAgentFactory;
import javafx.scene.paint.Color;

public interface DefaultValues {

    String APP_TITLE = "CAA Experimentation";

    String VERTICES_COLOR = "0x4eb3e5";
    String EDGES_COLOR = Color.BLACK.toString();
    boolean VERTICES_LABELS = true;
    boolean EDGES_LABELS = false;
    double EDGES_WIDTH = 1.0;
    String SHORTEST_PATH_COLOR = Color.ORANGE.toString();
    String STARTING_VERTEX_COLOR = Color.DARKBLUE.toString();
    String SAD_VERTEX_COLOR = Color.RED.toString();
    String HAPPY_VERTEX_COLOR = Color.GREEN.toString();

    double TRAVERSAL_INCREMENTER = -1.0;

    double SAD_INCREMENTER = -0.5;
    double SAD_INCREMENTER_MIN_VALUE = -10.0;
    double SAD_INCREMENTER_MAX_VALUE = -0.1;

    double HAPPY_INCREMENTER = 1.5;
    double HAPPY_INCREMENTER_MIN_VALUE = 1.1;
    double HAPPY_INCREMENTER_MAX_VALUE = 10.0;

    boolean SHOULD_ANIMATE_CAA = false;

    int NODES_SIZE = 42;
    int NODES_FONT_SIZE = 48;
    int NODES_FONT_STROKE_WIDTH = 1;
    String NODES_FONT_STROKE_COLOR = "#222";

    int EDGES_LENGTH = 275;
    int EDGES_FONT_SIZE = 24;
    double EDGES_WEIGHT = 0.0;
    double EDGES_TRAVERSAL_WEIGHT = 0.0;

    String GRAPH_VIS_TITLE = "Graph Visualization";
    String PREFERENCES_TITLE = "Preferences";

    String ABOUT_ALERT_TITLE = "About";
    String ABOUT_ALERT_HEADER = "About " + APP_TITLE;
    String ABOUT_CONTENT = APP_TITLE + " for experimenting with caa-enabled graphs:" +
            "\n - animate agent traversal," +
            "\n - animate caa experiment," +
            "\n - view shortest path." +
            "\n - view caa experiment statistics." +
            "\n\n" +
            "Copyright © 2019 Marjan Tanevski.";

    String GRAPH_STATS_TITLE = "Graph Statistics";

    CaaAgentFactory.CaaAgentType AGENT_TYPE_VALUE = CaaAgentFactory.CaaAgentType.ADVANCED;
    String SAMPLE_GRAPH_LOCATION = "sample-graph.graphml";
}
