package com.mtanevski.master.caa.gui.repositories;

import com.mtanevski.master.caa.gui.utils.RetentionFileChooser;
import org.springframework.stereotype.Repository;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

@Repository
public class RecentFilesRepository {

    private final Preferences preferences;

    public RecentFilesRepository() {
        preferences = Preferences.userNodeForPackage(RetentionFileChooser.class);
    }


    public List<String> getAll() {
        String recent = preferences.get(PreferencesConstants.RECENT_FILES, "");
        return Arrays.stream(recent.split(PreferencesConstants.SEPARATOR))
                .filter(file -> !file.isEmpty())
                .filter(file -> Files.exists(Paths.get(file)))
                .collect(Collectors.toList());
    }

    public void remove(String fileLocation) {
        String recent = preferences.get(PreferencesConstants.RECENT_FILES, "");
        String replaced = recent.replace(PreferencesConstants.SEPARATOR + fileLocation, "");
        preferences.put(PreferencesConstants.RECENT_FILES, replaced);
    }

    public void add(String fileLocation) {
        String existingPreferences = preferences.get(PreferencesConstants.RECENT_FILES, "");
        fileLocation = PreferencesConstants.SEPARATOR + fileLocation;
        if (existingPreferences.contains(fileLocation)) {
            existingPreferences = existingPreferences.replace(fileLocation, "");
        }
        String newPreferences = fileLocation + existingPreferences;
        preferences.put(PreferencesConstants.RECENT_FILES, newPreferences);
    }

}
