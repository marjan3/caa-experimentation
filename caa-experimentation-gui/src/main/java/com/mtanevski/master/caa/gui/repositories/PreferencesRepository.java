package com.mtanevski.master.caa.gui.repositories;

import com.mtanevski.master.caa.gui.DefaultValues;
import com.mtanevski.master.caa.gui.models.UserPreferences;
import javafx.scene.paint.Color;
import org.springframework.stereotype.Repository;

import java.util.prefs.Preferences;

@Repository
public class PreferencesRepository {

    private static final Preferences PREFERENCES = Preferences.userNodeForPackage(PreferencesRepository.class);

    public Color getVerticesColor() {
        return Color.valueOf(PREFERENCES.get(PreferencesConstants.VERTICES_COLOR, DefaultValues.VERTICES_COLOR.toString()));
    }

    public void setVerticesColor(Color value) {
        PREFERENCES.put(PreferencesConstants.VERTICES_COLOR, value.toString());
    }

    public boolean getShowVerticesLabels() {
        return PREFERENCES.getBoolean(PreferencesConstants.VERTICES_LABELS, DefaultValues.VERTICES_LABELS);
    }

    public void setShowVerticesLabels(Boolean value) {
        PREFERENCES.putBoolean(PreferencesConstants.VERTICES_LABELS, value);
    }

    public boolean getShowEdgesLabels() {
        return PREFERENCES.getBoolean(PreferencesConstants.EDGES_LABELS, DefaultValues.EDGES_LABELS);
    }

    public void setShowEdgesLabels(Boolean value) {
        PREFERENCES.putBoolean(PreferencesConstants.EDGES_LABELS, value);
    }

    public void setEdgesColors(Color value) {
        PREFERENCES.put(PreferencesConstants.EDGES_COLOR, value.toString());
    }

    private Color getEdgesColor() {
        return Color.valueOf(PREFERENCES.get(PreferencesConstants.EDGES_COLOR, DefaultValues.EDGES_COLOR.toString()));
    }

    public Color getShortestPathColor() {
        return Color.valueOf(PREFERENCES.get(PreferencesConstants.SHORTEST_PATH_COLOR, DefaultValues.SHORTEST_PATH_COLOR.toString()));
    }

    public void setShortestPathColor(Color value) {
        PREFERENCES.put(PreferencesConstants.SHORTEST_PATH_COLOR, value.toString());
    }

    public Color getStartingVertexColor() {
        return Color.valueOf(PREFERENCES.get(PreferencesConstants.STARTING_VERTEX_COLOR, DefaultValues.STARTING_VERTEX_COLOR.toString()));
    }

    public void setStartingVertexColor(Color value) {
        PREFERENCES.put(PreferencesConstants.STARTING_VERTEX_COLOR, value.toString());
    }

    public Color getSadVertexColor() {
        return Color.valueOf(PREFERENCES.get(PreferencesConstants.SAD_VERTEX_COLOR, DefaultValues.SAD_VERTEX_COLOR.toString()));
    }

    public void setSadVertexColor(Color value) {
        PREFERENCES.put(PreferencesConstants.SAD_VERTEX_COLOR, value.toString());
    }

    public Color getHappyVertexColor() {
        return Color.valueOf(PREFERENCES.get(PreferencesConstants.HAPPY_VERTEX_COLOR, DefaultValues.HAPPY_VERTEX_COLOR.toString()));
    }

    public void setHappyVertexColor(Color value) {
        PREFERENCES.put(PreferencesConstants.HAPPY_VERTEX_COLOR, value.toString());
    }

    public void setEdgesWidth(Number value) {
        PREFERENCES.putDouble(PreferencesConstants.EDGES_WIDTH, value.doubleValue());
    }

    public double getEdgesWith() {
        return PREFERENCES.getDouble(PreferencesConstants.EDGES_WIDTH, DefaultValues.EDGES_WIDTH);
    }

    public UserPreferences getDefaultPreferences() {
        UserPreferences userPreferences = new UserPreferences();

        userPreferences.setVerticesColor(DefaultValues.VERTICES_COLOR);
        userPreferences.setShowVerticesLabels(DefaultValues.VERTICES_LABELS);
        userPreferences.setEdgesColor(DefaultValues.EDGES_COLOR);
        userPreferences.setShowEdgesLabels(DefaultValues.EDGES_LABELS);
        userPreferences.setEdgesWidth(DefaultValues.EDGES_WIDTH);
        userPreferences.setShortestPathColor(DefaultValues.SHORTEST_PATH_COLOR);

        userPreferences.setStartingVertexColor(DefaultValues.STARTING_VERTEX_COLOR);
        userPreferences.setHappyVertexColor(DefaultValues.HAPPY_VERTEX_COLOR);
        userPreferences.setSadVertexColor(DefaultValues.SAD_VERTEX_COLOR);

        return userPreferences;
    }

    public UserPreferences getPreferences() {
        UserPreferences userPreferences = new UserPreferences();

        userPreferences.setVerticesColor(getVerticesColor());
        userPreferences.setShowVerticesLabels(getShowVerticesLabels());
        userPreferences.setEdgesColor(getEdgesColor());
        userPreferences.setShowEdgesLabels(getShowEdgesLabels());
        userPreferences.setEdgesWidth(getEdgesWith());
        userPreferences.setShortestPathColor(getShortestPathColor());

        userPreferences.setStartingVertexColor(getStartingVertexColor());
        userPreferences.setHappyVertexColor(getHappyVertexColor());
        userPreferences.setSadVertexColor(getSadVertexColor());

        return userPreferences;
    }
}
