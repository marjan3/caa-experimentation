package com.mtanevski.master.gui.alerts;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class LoadingAlert extends Dialog {

    private final ProgressIndicator indicator;
    private final Label label;
    private DoubleProperty progress = new SimpleDoubleProperty();

    public LoadingAlert(String name, ReadOnlyDoubleProperty numberProperty) {
        super();

        label = new Label("Loading BasicGraph...");
        indicator = new ProgressIndicator();
        indicator.progressProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("OLD: " + oldValue);
            System.out.println("NEW: " + newValue);
            if (newValue.doubleValue() == 1.0) {
                Window window = this.getDialogPane().getScene().getWindow();
                window.hide();
            }
        });
        Window window = this.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> window.hide());
        Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        indicator.progressProperty().bind(numberProperty);
        GridPane.setVgrow(indicator, Priority.ALWAYS);
        GridPane.setHgrow(indicator, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(indicator, 0, 1);
        this.initStyle(StageStyle.UTILITY);
        this.setGraphic(null);
        // Set expandable Exception into the dialog pane.
        this.getDialogPane().setContent(expContent);
        this.getDialogPane().getButtonTypes().clear();
    }
}
