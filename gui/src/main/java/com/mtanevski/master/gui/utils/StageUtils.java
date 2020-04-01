package com.mtanevski.master.gui.utils;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class StageUtils {

    public static void showModalStage(String title, Window owner, Parent parent) {
        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setResizable(false);
        Scene value = new Scene(parent);
        stage.setScene(value);
        stage.show();
    }

    public static void showModalToRight(String title, Window owner, Parent parent) {
        Stage stage = new Stage();
        stage.setX(owner.getX() + owner.getWidth());
        stage.setY(owner.getY());
        stage.setTitle(title);
        stage.setResizable(false);
        Scene value = new Scene(parent);
        stage.setScene(value);
        stage.show();
        owner.setOnCloseRequest((e) -> {
            stage.close();
        });
    }
}
