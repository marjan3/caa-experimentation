package com.mtanevski.master.caa.gui.utils;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;

public class FxmlLoader {

    public static <T> T load(String fxmlResourceLocation) {
        final URL location = FxmlLoader.class.getClassLoader().getResource(fxmlResourceLocation);
        final FXMLLoader loader = new FXMLLoader(location);
        T loaded;
        try {
            loaded = loader.load();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
        return loaded;
    }

    public static <T> T load(String fxmlResourceLocation, Object controller) {
        final URL location = FxmlLoader.class.getClassLoader().getResource(fxmlResourceLocation);
        final FXMLLoader loader = new FXMLLoader(location);
        loader.setController(controller);
        try {
            return loader.load();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

}
