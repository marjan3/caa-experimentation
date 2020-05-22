package com.mtanevski.master.caa.gui.controllers;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import org.springframework.stereotype.Component;

@Component
public class LoadingToolbarController {

    public ProgressBar progressBar;
    public Label loadingLbl;

    public void bindProgressBar(ReadOnlyDoubleProperty property) {
        progressBar.progressProperty().bind(property);
        progressBar.progressProperty().addListener((o, oldProp, newProp) -> {
            if (newProp.doubleValue() == 1.0) {
                this.progressBar.setVisible(false);
                this.loadingLbl.setVisible(false);
            }
        });
    }
}
