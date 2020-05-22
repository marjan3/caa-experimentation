package com.mtanevski.master.caa.gui.models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

public class UserPreferences {

    private final SimpleObjectProperty<Color> verticesColor = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Color> edgesColor = new SimpleObjectProperty<>();
    private final SimpleBooleanProperty showVerticesLabels = new SimpleBooleanProperty();
    private final SimpleBooleanProperty showEdgesLabels = new SimpleBooleanProperty();
    private final SimpleDoubleProperty edgesWidth = new SimpleDoubleProperty();
    private final SimpleObjectProperty<Color> shortestPathColor = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Color> startingVertexColor = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Color> sadVertexColor = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Color> happyVertexColor = new SimpleObjectProperty<>();

    public Color getVerticesColor() {
        return verticesColor.get();
    }

    public void setVerticesColor(Color verticesColor) {
        this.verticesColor.set(verticesColor);
    }

    public Color getEdgesColor() {
        return edgesColor.get();
    }

    public void setEdgesColor(Color edgesColor) {
        this.edgesColor.set(edgesColor);
    }

    public double getEdgesWidth() {
        return edgesWidth.get();
    }

    public void setEdgesWidth(double edgesWidth) {
        this.edgesWidth.set(edgesWidth);
    }

    public Color getShortestPathColor() {
        return shortestPathColor.get();
    }

    public void setShortestPathColor(Color shortestPathColor) {
        this.shortestPathColor.set(shortestPathColor);
    }

    public Color getStartingVertexColor() {
        return startingVertexColor.get();
    }

    public void setStartingVertexColor(Color startingVertexColor) {
        this.startingVertexColor.set(startingVertexColor);
    }

    public Color getSadVertexColor() {
        return sadVertexColor.get();
    }

    public void setSadVertexColor(Color sadVertexColor) {
        this.sadVertexColor.set(sadVertexColor);
    }

    public Color getHappyVertexColor() {
        return happyVertexColor.get();
    }

    public void setHappyVertexColor(Color happyVertexColor) {
        this.happyVertexColor.set(happyVertexColor);
    }

    public boolean getShowVerticesLabels() {
        return this.showVerticesLabels.get();
    }

    public void setShowVerticesLabels(boolean showVerticesLabels) {
        this.showVerticesLabels.set(showVerticesLabels);
    }

    public boolean getShowEdgesLabels() {
        return this.showEdgesLabels.get();
    }

    public void setShowEdgesLabels(boolean showEdgesLabels) {
        this.showEdgesLabels.set(showEdgesLabels);
    }
}
