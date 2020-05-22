package com.mtanevski.master.caa.gui.utils;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StageUtils {

    public static void showModalStage(String title, Parent parent) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setResizable(false);
        Scene value = new Scene(parent);
        stage.setScene(value);
        stage.show();
    }

}
