package com.mtanevski.master.caa.gui.controllers;

import com.mtanevski.master.caa.gui.models.UserPreferences;
import com.mtanevski.master.caa.gui.repositories.PreferencesRepository;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class PreferencesController {

    private final PreferencesRepository preferencesRepository;
    private final MessageSource messageSource;
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

    public PreferencesController(PreferencesRepository repository, MessageSource messageSource) {
        this.preferencesRepository = repository;
        this.messageSource = messageSource;
    }

    @FXML
    public void initialize() {
        this.verticesLabels.selectedProperty().addListener((b, o, n) -> this.verticesLabels.setText(toggleLabel(n)));
        this.edgesLabels.selectedProperty().addListener((b, o, n) -> this.edgesLabels.setText(toggleLabel(n)));

        UserPreferences preferences = preferencesRepository.getPreferences();
        this.verticesLabels.setText(toggleLabel(preferences.getShowVerticesLabels()));
        this.edgesLabels.setText(toggleLabel(preferences.getShowEdgesLabels()));

        this.verticesColor.setValue(preferences.getVerticesColor());
        this.edgesColor.setValue(preferences.getEdgesColor());
        this.verticesLabels.setSelected(preferences.getShowVerticesLabels());
        this.edgesLabels.setSelected(preferences.getShowEdgesLabels());
        this.edgesWidth.setValue(preferences.getEdgesWidth());
        this.shortestPathColor.setValue(preferences.getShortestPathColor());
        this.startingVertexColor.setValue(preferences.getStartingVertexColor());
        this.happyVertexColor.setValue(preferences.getHappyVertexColor());
        this.sadVertexColor.setValue(preferences.getSadVertexColor());

        // general preferences
        this.verticesColor.valueProperty().addListener((b, o, n) -> preferencesRepository.setVerticesColor(n));
        this.edgesColor.valueProperty().addListener((b, o, n) -> preferencesRepository.setEdgesColors(n));
        this.verticesLabels.selectedProperty().addListener((b, o, n) -> preferencesRepository.setShowVerticesLabels(n));
        this.edgesLabels.selectedProperty().addListener((b, o, n) -> preferencesRepository.setShowEdgesLabels(n));
        this.edgesWidth.valueProperty().addListener((b, o, n) -> preferencesRepository.setEdgesWidth(n));
        this.shortestPathColor.valueProperty().addListener((b, o, n) -> preferencesRepository.setShortestPathColor(n));

        // CAA preferences
        this.startingVertexColor.valueProperty().addListener((b, o, n) -> preferencesRepository.setStartingVertexColor(n));
        this.happyVertexColor.valueProperty().addListener((b, o, n) -> preferencesRepository.setHappyVertexColor(n));
        this.sadVertexColor.valueProperty().addListener((b, o, n) -> preferencesRepository.setSadVertexColor(n));
    }

    @FXML
    public void resetGeneralPreferences() {
        UserPreferences defaultUserPreferences = preferencesRepository.getDefaultPreferences();
        this.verticesColor.setValue(defaultUserPreferences.getVerticesColor());
        this.edgesColor.setValue(defaultUserPreferences.getEdgesColor());
        this.verticesLabels.setSelected(defaultUserPreferences.getShowVerticesLabels());
        this.edgesLabels.setSelected(defaultUserPreferences.getShowEdgesLabels());
        this.edgesWidth.setValue(defaultUserPreferences.getEdgesWidth());
        this.shortestPathColor.setValue(defaultUserPreferences.getShortestPathColor());
    }

    @FXML
    public void resetCaaPreferences() {
        UserPreferences defaultUserPreferences = preferencesRepository.getDefaultPreferences();
        this.startingVertexColor.setValue(defaultUserPreferences.getStartingVertexColor());
        this.sadVertexColor.setValue(defaultUserPreferences.getSadVertexColor());
        this.happyVertexColor.setValue(defaultUserPreferences.getHappyVertexColor());
    }

    private String toggleLabel(Boolean isOn) {
        String on = messageSource.getMessage("preferences.dialog.on", null, Locale.getDefault());
        String off = messageSource.getMessage("preferences.dialog.off", null, Locale.getDefault());
        return isOn ? on : off;
    }
}
