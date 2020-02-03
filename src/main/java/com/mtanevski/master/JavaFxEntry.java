package com.mtanevski.master;

import com.mtanevski.master.fxml.utils.FxmlLoader;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static com.mtanevski.master.fxml.DefaultValues.APP_TITLE;

public class JavaFxEntry extends Application {

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) throws Exception {

        final Parent root = FxmlLoader.load("fxml/index.fxml");
        final Scene scene = new Scene(root, 900, 725);

        stage.setScene(scene);
        stage.setTitle(APP_TITLE);

        stage.show();
    }
}
