package com.mtanevski.master.caa.gui.models;

import com.mtanevski.master.caa.lib.CaaExperimentInput;
import com.mtanevski.master.caa.lib.CaaExperimentResults;

public class CaaExperimentData {

    private final int version;
    private final CaaExperimentInput experiment;
    private final CaaExperimentResults results;

    public CaaExperimentData(CaaExperimentInput experiment, CaaExperimentResults results) {
        this.experiment = experiment;
        this.results = results;
        this.version = 1;
    }

    public CaaExperimentInput getExperiment() {
        return experiment;
    }

    public CaaExperimentResults getResults() {
        return results;
    }

    public int getVersion() {
        return version;
    }
}
