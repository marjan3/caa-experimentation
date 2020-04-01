package com.mtanevski.master.gui.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mtanevski.master.gui.alerts.ErrorAlert;
import com.mtanevski.master.lib.caa.CaaEdge;
import com.mtanevski.master.lib.caa.CaaExperimenter;
import com.mtanevski.master.lib.caa.CaaGraph;
import com.mtanevski.master.lib.caa.CaaTraversalData;
import com.mtanevski.master.lib.caa.impl.graph.readonly.CaaGraphSnapshot;
import com.mtanevski.master.lib.caa.impl.graph.readonly.WeightedCaaEdge;
import com.mtanevski.master.lib.caa.impl.graph.tinker.TinkerCaaGraph;
import com.mtanevski.master.lib.caa.impl.experiment.CaaExperimenterImpl;
import com.mtanevski.master.lib.caa.impl.experiment.traversal.CaaTraversalDataImpl;
import javafx.application.Platform;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import static com.mtanevski.master.gui.DefaultValues.SAMPLE_GRAPH_LOCATION;

public class SessionManager {

    /*
     * Working session related objects.
     * They describe the current status of the session
     */
    private static CaaGraph caaGraph;
    private static CaaExperimenter caaExperimenter;
    private static File chosenGraphFile;
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(CaaGraph.class, InterfaceSerializer.interfaceSerializer(CaaGraphSnapshot.class))
            .registerTypeAdapter(CaaEdge.class, InterfaceSerializer.interfaceSerializer(WeightedCaaEdge.class))
            .registerTypeAdapter(CaaTraversalData.class, InterfaceSerializer.interfaceSerializer(CaaTraversalDataImpl.class))
            .setLenient()
            .create();
    private static Map<Integer, CaaExperimenter> experimenters;

    static {
        chosenGraphFile = new File(SAMPLE_GRAPH_LOCATION);
        experimenters = new HashMap<>();
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

    public static CaaExperimenter experimenter() {
        if (caaExperimenter == null) {
            if (caaGraph == null) {
                throw new IllegalStateException("No graph to execute experiments on");
            }
            caaExperimenter = new CaaExperimenterImpl(caaGraph);
        }
        return caaExperimenter;
    }

    public static void rememberExperimenter(Integer id, CaaExperimenter caaExperimenter) {
        experimenters.put(id, SessionManager.caaExperimenter);
    }

    public static CaaExperimenter getRememberedExperiment(Integer id) {
        return experimenters.get(id);
    }

    public static CaaExperimenter newExperimenter(Double happyMultiplier, Double sadMultiplier) {
        if (caaGraph == null) {
            throw new IllegalStateException("No graph to execute experiments on");
        }
        caaExperimenter = new CaaExperimenterImpl(caaGraph, happyMultiplier, sadMultiplier);
        return caaExperimenter;
    }

    public static void saveData(File file, CaaExperimenter caaExperimenter) {
        if (caaExperimenter == null) {
            throw new IllegalStateException("No caa experiment found");
        }

        String caaExperimenterData = gson.toJson(caaExperimenter);

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

    public static CaaExperimenterImpl loadCaaExperimenterFromFile(File chosenFile) {
        try {
            return gson.fromJson(Files.newBufferedReader(chosenFile.toPath()), CaaExperimenterImpl.class);
        } catch (Exception anyException) {
            ErrorAlert errorAlert = new ErrorAlert("Can not load caa experiment results from file", "Invalid results file provided", anyException.getMessage());
            errorAlert.showAndWait();
            throw new IllegalStateException(anyException);
        }
    }
}
