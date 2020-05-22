package com.mtanevski.master.caa.gui.services;

import com.mtanevski.master.caa.gui.DefaultValues;
import com.mtanevski.master.caa.gui.alerts.ErrorAlert;
import com.mtanevski.master.caa.gui.models.CaaExperimentData;
import com.mtanevski.master.caa.lib.CaaExperimentInput;
import com.mtanevski.master.caa.lib.CaaExperimentResults;
import com.mtanevski.master.caa.lib.CaaExperimenter;
import com.mtanevski.master.caa.lib.CaaGraph;
import com.mtanevski.master.caa.lib.impl.agents.CaaAgentType;
import com.mtanevski.master.caa.lib.impl.graphs.tinker.TinkerCaaGraph;
import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
public class CaaSessionService {

    private final CaaExperimenter caaExperimenter;
    /*
     * Working session related objects.
     * They describe the current status of the session
     */
    private CaaGraph openGraph;
    private File openGraphFile;
    private CaaExperimentData executedExperiment;
    private final Map<Integer, CaaExperimentData> experiments = new HashMap<>();

    private CaaSessionService(@Value("sample.graph.location") String sampleGraphLocation) {
        openGraphFile = new File(sampleGraphLocation);
        caaExperimenter = new CaaExperimenter();
    }

    public void openGraph(String fileLocation) {
        try {
            openGraphFile = new File(fileLocation);
            openGraph = new TinkerCaaGraph(openGraphFile.getAbsolutePath(), false);
        } catch (Exception anyException) {
            ErrorAlert errorAlert = new ErrorAlert("Can not load graph", "Invalid graph provided", anyException.getMessage());
            errorAlert.showAndWait();
            Platform.exit();
            throw new IllegalStateException(anyException);
        }
    }

    public File getOpenGraphFile() {
        if (openGraphFile == null) {
            throw new IllegalStateException("Graph file not loaded. Can not proceed further");
        }
        return openGraphFile;
    }

    public CaaGraph getOpenGraph() {
        if (openGraph == null) {
            throw new IllegalStateException("Graph not loaded. Can not proceed further");
        }
        return openGraph;
    }

    public CaaExperimentData getExecutedExperiment() {
        if (executedExperiment.getExperiment() == null) {
            if (openGraph == null) {
                throw new IllegalStateException("No graph to execute experiments on");
            }
            return executeExperiment(
                    DefaultValues.HAPPY_INCREMENTER,
                    DefaultValues.SAD_INCREMENTER,
                    DefaultValues.AGENT_TYPE_VALUE);
        }
        return executedExperiment;
    }

    public CaaExperimentData executeExperiment(
            Double happyMultiplier,
            Double sadMultiplier,
            CaaAgentType caaAgentType) {
        if (openGraph == null) {
            throw new IllegalStateException("No graph to execute experiments on");
        }

        CaaExperimentInput experiment = CaaExperimentInput.create(caaAgentType, happyMultiplier, sadMultiplier);

        CaaExperimentResults results = caaExperimenter.executeExperiment(this.getOpenGraph(), experiment);
        executedExperiment = new CaaExperimentData(experiment, results);
        return executedExperiment;
    }

    public void setRememberedExperiment(int id, CaaExperimentData experimentData) {
        experiments.put(id, experimentData);
    }

    public CaaExperimentData getRememberedExperiment(Integer id) {
        return experiments.get(id);
    }

}
