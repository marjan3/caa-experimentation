package com.mtanevski.master.gui.alerts;

import javafx.scene.control.Alert;

public class ErrorAlert extends Alert {
    public ErrorAlert(String title, String header, String content) {
        super(AlertType.ERROR);

        this.setTitle(title);
        this.setHeaderText(header);
        this.setContentText(content);
    }
}
