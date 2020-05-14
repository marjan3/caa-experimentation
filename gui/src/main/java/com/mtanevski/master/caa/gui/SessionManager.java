package com.mtanevski.master.caa.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mtanevski.master.caa.gui.alerts.ErrorAlert;
import com.mtanevski.master.caa.gui.utils.InterfaceSerializer;
import com.mtanevski.master.caa.lib.CaaExperimentInput;
import com.mtanevski.master.caa.lib.CaaExperimentResults;
import com.mtanevski.master.caa.lib.CaaExperimenter;
import com.mtanevski.master.caa.lib.CaaGraph;
import com.mtanevski.master.caa.lib.impl.agents.CaaAgentType;
import com.mtanevski.master.caa.lib.impl.graphs.readonly.ImmutableCaaGraph;
import com.mtanevski.master.caa.lib.impl.graphs.tinker.TinkerCaaGraph;
import javafx.application.Platform;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class SessionManager {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(CaaGraph.class, InterfaceSerializer.interfaceSerializer(ImmutableCaaGraph.class))
            .setLenient()
            .create();
    private static final Map<Integer, CaaExperimentSaveData> experiments;
    /*
     * Working session related objects.
     * They describe the current status of the session
     */
    private static CaaGraph caaGraph;
    private static CaaExperimentSaveData caaExperimentSaveData;
    private static File chosenGraphFile;

    static {
        chosenGraphFile = new File(DefaultValues.SAMPLE_GRAPH_LOCATION);
        experiments = new HashMap<>();
    }

    private SessionManager() {
    }

    public static boolean loadGraph(String fileLocation) {
        try {
            chosenGraphFile = new File(fileLocation);
            caaGraph = new TinkerCaaGraph(chosenGraphFile.getAbsolutePath(), false);
            return true;
        } catch (Exception anyException) {
            ErrorAlert errorAlert = new ErrorAlert("Can not load graph", "Invalid graph provided", anyException.getMessage());
            errorAlert.showAndWait();
            Platform.exit();
            throw new IllegalStateException(anyException);
        }
    }

    public static File graphFile() {
        if (chosenGraphFile == null) {
            throw new IllegalStateException("Graph file not loaded. Can not proceed further");
        }
        return chosenGraphFile;
    }

    public static CaaGraph graph() {
        if (caaGraph == null) {
            throw new IllegalStateException("Graph not loaded. Can not proceed further");
        }
        return caaGraph;
    }

    public static CaaExperimentSaveData experiment() {
        if (caaExperimentSaveData.getExperiment() == null) {
            if (caaGraph == null) {
                throw new IllegalStateException("No graph to execute experiments on");
            }
            return newExperiment(
                    DefaultValues.HAPPY_INCREMENTER,
                    DefaultValues.SAD_INCREMENTER,
                    DefaultValues.AGENT_TYPE_VALUE);
        }
        return caaExperimentSaveData;
    }

    public static void rememberExperiment(Integer id, CaaExperimentSaveData caaExperimentSaveData) {
        experiments.put(id, caaExperimentSaveData);
    }

    public static CaaExperimentSaveData getRememberedExperiment(Integer id) {
        return experiments.get(id);
    }

    public static CaaExperimentSaveData newExperiment(
            Double happyMultiplier,
            Double sadMultiplier,
            CaaAgentType caaAgentType) {
        if (caaGraph == null) {
            throw new IllegalStateException("No graph to execute experiments on");
        }

        CaaExperimentInput experiment = CaaExperimentInput.create(caaAgentType, happyMultiplier, sadMultiplier);

        CaaExperimentResults results = CaaExperimenter.execute(SessionManager.graph(), experiment);
        caaExperimentSaveData = new CaaExperimentSaveData(experiment, results);
        return caaExperimentSaveData;
    }

    public static void saveData(File file, CaaExperimentSaveData caaExperimentSaveData) {
        if (caaExperimentSaveData == null) {
            throw new IllegalStateException("No caa experiment found");
        }

        String caaExperimenterData = gson.toJson(caaExperimentSaveData);

        try {
            Path filePath = Paths.get(file.getAbsolutePath());
            Files.deleteIfExists(filePath);
            Files.write(filePath, caaExperimenterData.getBytes(), StandardOpenOption.CREATE);
        } catch (Exception anyException) {
            ErrorAlert errorAlert = new ErrorAlert("Can not write caa experiment results from file", "Error occurred while witting to file", anyException.getMessage());
            errorAlert.showAndWait();
            throw new IllegalStateException(anyException);
        }
    }

    public static CaaExperimentSaveData loadCaaExperimenterFromFile(File chosenFile) {
        try {
            return gson.fromJson(Files.newBufferedReader(chosenFile.toPath()), CaaExperimentSaveData.class);
        } catch (Exception anyException) {
            ErrorAlert errorAlert = new ErrorAlert("Can not load caa experiment results from file", "Invalid results file provided", anyException.getMessage());
            errorAlert.showAndWait();
            throw new IllegalStateException(anyException);
        }
    }
}
