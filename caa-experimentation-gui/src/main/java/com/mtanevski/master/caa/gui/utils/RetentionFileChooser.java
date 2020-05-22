package com.mtanevski.master.caa.gui.utils;

import com.mtanevski.master.caa.gui.DefaultValues;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

public class RetentionFileChooser {

    private static final SimpleObjectProperty<File> lastKnownDirectoryProperty = new SimpleObjectProperty<>();
    private static FileChooser instance = null;

    private RetentionFileChooser() {
    }

    public static File showOpenGraphDialog(Window ownerWindow) {
        return showOpenDialog(ownerWindow, DefaultValues.FilterMode.GRAPHML_FILES);
    }

    public static File showOpenResultsDialog(Window ownerWindow) {
        return showOpenDialog(ownerWindow, DefaultValues.FilterMode.RESULTS_FILES);
    }

    public static File showSaveResultsDialog(String initialFileName) {
        FileChooser instance = getInstance(DefaultValues.FilterMode.RESULTS_FILES);
        RetentionFileChooser.instance.setInitialFileName(initialFileName);
        return instance.showSaveDialog(null);
    }

    private static File showOpenDialog(Window ownerWindow, DefaultValues.FilterMode... filterModes) {
        File chosenFile = getInstance(filterModes).showOpenDialog(ownerWindow);
        if (chosenFile != null) {
            lastKnownDirectoryProperty.setValue(chosenFile.getParentFile());
        }
        return chosenFile;
    }

    private static FileChooser getInstance(DefaultValues.FilterMode... filterModes) {
        if (instance == null) {
            instance = new FileChooser();
            instance.initialDirectoryProperty().bindBidirectional(lastKnownDirectoryProperty);
        }
        //Set the filters to those provided
        //You could addEdge check's to ensure that a default filter is included, adding it if need be
        instance.getExtensionFilters().setAll(
                Arrays.stream(filterModes)
                        .map(DefaultValues.FilterMode::getExtensionFilter)
                        .collect(Collectors.toList()));
        return instance;
    }
}
