package com.mtanevski.master.caa.gui.alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class TextAlert extends Alert {
    public TextAlert(String title, String header, String content) {
        super(AlertType.INFORMATION);

        this.setTitle(title);
        this.setHeaderText(header);

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);

        TextArea textArea = new TextArea(content);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);

        this.getDialogPane().setContent(expContent);
    }
}

