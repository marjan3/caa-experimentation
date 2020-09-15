package com.mtanevski.master.caa.gui.alerts;

import javafx.scene.control.Alert;

public class InformationAlert extends Alert {
    public InformationAlert(String title, String header, String content) {
        super(AlertType.INFORMATION);

        this.setTitle(title);
        this.setHeaderText(header);
        this.setContentText(content);
    }
}

