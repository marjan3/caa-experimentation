package com.mtanevski.master.fxml.utils;

import com.mtanevski.master.fxml.preferences.PreferencesConstants;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

public class RetentionFileChooser {

    private static Preferences preferences;
    private static FileChooser instance = null;
    private static SimpleObjectProperty<File> lastKnownDirectoryProperty = new SimpleObjectProperty<>();

    static {
        preferences = Preferences.userNodeForPackage(RetentionFileChooser.class);
    }
    private RetentionFileChooser() {
    }

    public static File showOpenGraphDialog(Window ownerWindow) {
        return showOpenDialog(ownerWindow, FilterMode.GRAPHML_FILES);
    }
    public static File showOpenResultsDialog(Window ownerWindow) {
        return showOpenDialog(ownerWindow, FilterMode.RESULTS_FILES);
    }

    public static File showSaveResultsDialog(String initialFileName, Window window) {
        FileChooser instance = getInstance(FilterMode.RESULTS_FILES);
        RetentionFileChooser.instance.setInitialFileName(initialFileName);
        return instance.showSaveDialog(window);
    }

    public static File showOpenDialog(FilterMode... filterModes) {
        return showOpenDialog(null, filterModes);
    }

    private static File showOpenDialog(Window ownerWindow, FilterMode... filterModes) {
        File chosenFile = getInstance(filterModes).showOpenDialog(ownerWindow);
        if (chosenFile != null) {
            String path = chosenFile.getAbsolutePath();
            String existingPreferences = preferences.get(PreferencesConstants.RECENT_FILES, "");
            path = PreferencesConstants.SEPARATOR + path;
            if (existingPreferences.contains(path)) {
                existingPreferences = existingPreferences.replace(path, "");
            }
            String newPreferences = path + existingPreferences;
            preferences.put(PreferencesConstants.RECENT_FILES, newPreferences);
            lastKnownDirectoryProperty.setValue(chosenFile.getParentFile());
        }
        return chosenFile;
    }

    private static FileChooser getInstance(FilterMode... filterModes) {
        if (instance == null) {
            instance = new FileChooser();
            instance.initialDirectoryProperty().bindBidirectional(lastKnownDirectoryProperty);
        }
        //Set the filters to those provided
        //You could addEdge check's to ensure that a default filter is included, adding it if need be
        instance.getExtensionFilters().setAll(
                Arrays.stream(filterModes)
                        .map(FilterMode::getExtensionFilter)
                        .collect(Collectors.toList()));
        return instance;
    }

    public static List<String> getRecentFiles() {
        String recent = preferences.get(PreferencesConstants.RECENT_FILES, "");
        return Arrays.stream(recent.split(PreferencesConstants.SEPARATOR))
                .filter(file -> !file.isEmpty())
                .filter(file -> Files.exists(Paths.get(file)))
                .collect(Collectors.toList());
    }

    public static void removeFromRecentFiles(String toRemove) {
        String recent = preferences.get(PreferencesConstants.RECENT_FILES, "");
        String replaced = recent.replace(PreferencesConstants.SEPARATOR + toRemove, "");
        preferences.put(PreferencesConstants.RECENT_FILES, replaced);
    }

    public enum FilterMode {
        //Setup supported filters
        GRAPHML_FILES("GraphML files (*.graphml)", "*.graphml"),
        RESULTS_FILES("Results (JSON)", "*.json");

        private FileChooser.ExtensionFilter extensionFilter;

        FilterMode(String extensionDisplayName, String... extensions) {
            extensionFilter = new FileChooser.ExtensionFilter(extensionDisplayName, extensions);
        }

        public FileChooser.ExtensionFilter getExtensionFilter() {
            return extensionFilter;
        }
    }
}
