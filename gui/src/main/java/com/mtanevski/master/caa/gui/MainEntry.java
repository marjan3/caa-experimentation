package com.mtanevski.master.caa.gui;

import com.mtanevski.master.caa.gui.utils.FxmlLoader;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainEntry extends Application {

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) {

        final Parent root = FxmlLoader.load("fxml/index.fxml");
        final Scene scene = new Scene(root, 900, 725);

        stage.setScene(scene);
        stage.setTitle(DefaultValues.APP_TITLE);

        stage.show();
    }
}
