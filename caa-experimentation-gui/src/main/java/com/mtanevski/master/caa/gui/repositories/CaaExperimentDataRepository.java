package com.mtanevski.master.caa.gui.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mtanevski.master.caa.gui.alerts.ErrorAlert;
import com.mtanevski.master.caa.gui.models.CaaExperimentData;
import com.mtanevski.master.caa.gui.utils.InterfaceSerializer;
import com.mtanevski.master.caa.lib.CaaGraph;
import com.mtanevski.master.caa.lib.impl.graphs.readonly.ImmutableCaaGraph;
import org.springframework.stereotype.Repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Repository
public class CaaExperimentDataRepository {

    private final Gson gson;

    public CaaExperimentDataRepository() {
        gson = new GsonBuilder()
                .registerTypeAdapter(CaaGraph.class,
                        InterfaceSerializer.interfaceSerializer(ImmutableCaaGraph.class))
                .setLenient()
                .create();
    }

    public void store(String id, CaaExperimentData caaExperimentData) throws IllegalStateException {
        try {
            String jsonData = this.gson.toJson(caaExperimentData);
            try {
                Path filePath = Paths.get(id);
                Files.deleteIfExists(filePath);
                Files.write(filePath, jsonData.getBytes(), StandardOpenOption.CREATE);
            } catch (Exception anyException) {
                ErrorAlert errorAlert = new ErrorAlert("Can not write caa experiment results from file", "Error occurred while witting to file", anyException.getMessage());
                errorAlert.showAndWait();
                throw new IllegalStateException(anyException);
            }
        } catch (Exception anyException) {
            throw new IllegalStateException(anyException.getMessage());
        }
    }

    public CaaExperimentData read(String id) throws IllegalStateException {
        try {
            return this.gson.fromJson(Files.newBufferedReader(Paths.get(id)), CaaExperimentData.class);
        } catch (Exception anyException) {
            throw new IllegalStateException(anyException.getMessage());
        }
    }
}
