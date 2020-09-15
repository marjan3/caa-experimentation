package com.mtanevski.master.caa.gui;

import com.mtanevski.master.caa.lib.impl.agents.CaaAgentType;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

/**
 * Overall default values for the whole application
 */
public interface DefaultValues {

    // window size
    Integer WINDOW_WIDTH = 800, WINDOW_HEIGHT = 600;

    // preferences
    Color VERTICES_COLOR = Color.web("0x4eb3e5");
    Color EDGES_COLOR = Color.BLACK;
    boolean VERTICES_LABELS = true;
    boolean EDGES_LABELS = false;
    double EDGES_WIDTH = 1.0;
    Color SHORTEST_PATH_COLOR = Color.ORANGE;
    Color STARTING_VERTEX_COLOR = Color.DARKBLUE;
    Color SAD_VERTEX_COLOR = Color.RED;
    Color HAPPY_VERTEX_COLOR = Color.GREEN;

    // vis
    int NODES_SIZE = 42;
    int NODES_FONT_SIZE = 48;
    int NODES_FONT_STROKE_WIDTH = 1;
    String NODES_FONT_STROKE_COLOR = "#222";
    int EDGES_LENGTH = 275;
    int EDGES_FONT_SIZE = 24;

    // caa experiment
    double SAD_INCREMENTER = -0.5;
    double SAD_INCREMENTER_MIN_VALUE = -10.0;
    double SAD_INCREMENTER_MAX_VALUE = -0.1;
    double HAPPY_INCREMENTER = 1.5;
    double HAPPY_INCREMENTER_MIN_VALUE = 1.1;
    double HAPPY_INCREMENTER_MAX_VALUE = 10.0;
    boolean SHOULD_ANIMATE_CAA = false;
    CaaAgentType AGENT_TYPE_VALUE = CaaAgentType.ORIGINAL;

    // Supported file types
    enum FilterMode {
        //Setup supported filters
        GRAPHML_FILES("GraphML files (*.graphml)", "*.graphml"),
        RESULTS_FILES("Results (JSON)", "*.json");

        private final FileChooser.ExtensionFilter extensionFilter;

        FilterMode(String extensionDisplayName, String... extensions) {
            extensionFilter = new FileChooser.ExtensionFilter(extensionDisplayName, extensions);
        }

        public FileChooser.ExtensionFilter getExtensionFilter() {
            return extensionFilter;
        }
    }

    // Results file name extension
    String RESULTS_FILE_NAME_EXTENSION = ".caa-experiment-results.json";
}
