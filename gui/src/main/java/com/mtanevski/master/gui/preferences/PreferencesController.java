package com.mtanevski.master.gui.preferences;

import com.mtanevski.master.gui.DefaultValues;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;

import java.util.prefs.Preferences;

public class PreferencesController {

    private static Preferences p = Preferences.userNodeForPackage(PreferencesController.class);

    @FXML
    public ColorPicker verticesColor;

    @FXML
    public ColorPicker edgesColor;

    @FXML
    public ToggleButton verticesLabels;

    @FXML
    public ToggleButton edgesLabels;

    @FXML
    public Slider edgesWidth;

    @FXML
    public ColorPicker shortestPathColor;

    @FXML
    public ColorPicker startingVertexColor;

    @FXML
    public ColorPicker sadVertexColor;

    @FXML
    public ColorPicker happyVertexColor;


    private static String toggleLabel(Boolean n) {
        return n ? "ON" : "OFF";
    }

    @FXML
    public void initialize() {
        this.verticesLabels.selectedProperty().addListener((b, o, n) -> this.verticesLabels.setText(toggleLabel(n)));
        this.edgesLabels.selectedProperty().addListener((b, o, n) -> this.edgesLabels.setText(toggleLabel(n)));
        this.edgesWidth.valueProperty().addListener((b, o, n) -> p.putDouble(PreferencesConstants.EDGES_WIDTH, this.edgesWidth.getValue()));

        this.verticesColor.setValue(Color.web(p.get(PreferencesConstants.VERTICES_COLOR, DefaultValues.VERTICES_COLOR)));
        this.edgesColor.setValue(Color.web(p.get(PreferencesConstants.EDGES_COLOR, DefaultValues.EDGES_COLOR)));
        this.verticesLabels.setText(toggleLabel(p.getBoolean(PreferencesConstants.VERTICES_LABELS, DefaultValues.VERTICES_LABELS)));
        this.verticesLabels.setSelected(p.getBoolean(PreferencesConstants.VERTICES_LABELS, DefaultValues.VERTICES_LABELS));
        this.edgesLabels.setText(toggleLabel(p.getBoolean(PreferencesConstants.EDGES_LABELS, DefaultValues.EDGES_LABELS)));
        this.edgesLabels.setSelected(p.getBoolean(PreferencesConstants.EDGES_LABELS, DefaultValues.EDGES_LABELS));
        this.edgesWidth.setValue(p.getDouble(PreferencesConstants.EDGES_WIDTH, DefaultValues.EDGES_WIDTH));
        this.shortestPathColor.setValue(Color.web(p.get(PreferencesConstants.SHORTEST_PATH_COLOR, DefaultValues.SHORTEST_PATH_COLOR)));

        this.startingVertexColor.setValue(Color.web(p.get(PreferencesConstants.STARTING_VERTEX_COLOR, DefaultValues.STARTING_VERTEX_COLOR)));
        this.sadVertexColor.setValue(Color.web(p.get(PreferencesConstants.SAD_VERTEX_COLOR, DefaultValues.SAD_VERTEX_COLOR)));
        this.happyVertexColor.setValue(Color.web(p.get(PreferencesConstants.HAPPY_VERTEX_COLOR, DefaultValues.HAPPY_VERTEX_COLOR)));

    }

    @FXML
    public void resetGeneralPreferences(ActionEvent actionEvent) {
        this.verticesColor.setValue(Color.web(DefaultValues.VERTICES_COLOR));
        p.put(PreferencesConstants.VERTICES_COLOR, this.verticesColor.getValue().toString());
        this.edgesColor.setValue(Color.web(DefaultValues.EDGES_COLOR));
        p.put(PreferencesConstants.EDGES_COLOR, this.edgesColor.getValue().toString());
        this.verticesLabels.setSelected(DefaultValues.VERTICES_LABELS);
        p.putBoolean(PreferencesConstants.VERTICES_LABELS, this.verticesLabels.selectedProperty().get());
        this.edgesLabels.setSelected(DefaultValues.EDGES_LABELS);
        p.putBoolean(PreferencesConstants.EDGES_LABELS, this.edgesLabels.selectedProperty().get());
        this.edgesWidth.setValue(DefaultValues.EDGES_WIDTH);
        p.putDouble(PreferencesConstants.EDGES_WIDTH, this.edgesWidth.getValue());
        this.shortestPathColor.setValue(Color.web(DefaultValues.SHORTEST_PATH_COLOR));
        p.put(PreferencesConstants.SHORTEST_PATH_COLOR, this.shortestPathColor.getValue().toString());

    }

    @FXML
    public void resetCaaPreferences(ActionEvent actionEvent) {
        this.startingVertexColor.setValue(Color.web(DefaultValues.STARTING_VERTEX_COLOR));
        p.put(PreferencesConstants.STARTING_VERTEX_COLOR, this.startingVertexColor.getValue().toString());
        this.sadVertexColor.setValue(Color.web(DefaultValues.SAD_VERTEX_COLOR));
        p.put(PreferencesConstants.SAD_VERTEX_COLOR, this.sadVertexColor.getValue().toString());
        this.happyVertexColor.setValue(Color.web(DefaultValues.HAPPY_VERTEX_COLOR));
        p.put(PreferencesConstants.HAPPY_VERTEX_COLOR, this.happyVertexColor.getValue().toString());
    }

    @FXML
    public void setVerticesColor(ActionEvent actionEvent) {
        p.put(PreferencesConstants.VERTICES_COLOR, this.verticesColor.getValue().toString());
    }

    @FXML
    public void setEdgesColor(ActionEvent actionEvent) {
        p.put(PreferencesConstants.EDGES_COLOR, this.edgesColor.getValue().toString());

    }

    @FXML
    public void toggleVerticesLabels(ActionEvent actionEvent) {
        p.putBoolean(PreferencesConstants.VERTICES_LABELS, this.verticesLabels.selectedProperty().get());
    }

    @FXML
    public void toggleEdgesLabels(ActionEvent actionEvent) {
        p.putBoolean(PreferencesConstants.EDGES_LABELS, this.edgesLabels.selectedProperty().get());
    }

    @FXML
    public void setShortestPathColor(){
        p.put(PreferencesConstants.SHORTEST_PATH_COLOR, this.shortestPathColor.getValue().toString());
    }

    @FXML
    public void setStartingVertexColor(ActionEvent actionEvent) {
        p.put(PreferencesConstants.STARTING_VERTEX_COLOR, this.startingVertexColor.getValue().toString());

    }

    @FXML
    public void setSadVertexColor(ActionEvent actionEvent) {
        p.put(PreferencesConstants.SAD_VERTEX_COLOR, this.sadVertexColor.getValue().toString());

    }

    @FXML
    public void setHappyVertexColor(ActionEvent actionEvent) {
        p.put(PreferencesConstants.HAPPY_VERTEX_COLOR, this.happyVertexColor.getValue().toString());
    }
}
