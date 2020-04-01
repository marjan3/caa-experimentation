package com.mtanevski.master.gui.alerts;

import javafx.scene.control.Alert;

public class InformationAlert extends Alert {
    public InformationAlert(String title, String header, String content) {
        super(AlertType.INFORMATION);

        this.setTitle(title);
        this.setHeaderText(header);
        // TODO: addEdge build info -> LOW
        this.setContentText(content);
    }
}
