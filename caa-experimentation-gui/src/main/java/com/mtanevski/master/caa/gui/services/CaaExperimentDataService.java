package com.mtanevski.master.caa.gui.services;

import com.mtanevski.master.caa.gui.alerts.ErrorAlert;
import com.mtanevski.master.caa.gui.models.CaaExperimentData;
import com.mtanevski.master.caa.gui.repositories.CaaExperimentDataRepository;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class CaaExperimentDataService {

    private final CaaExperimentDataRepository repository;

    public CaaExperimentDataService(CaaExperimentDataRepository repository) {
        this.repository = repository;
    }

    public void saveExperimentData(File file, CaaExperimentData caaExperimentData) {
        if (caaExperimentData == null) {
            throw new IllegalStateException("No caa experiment found");
        }
        try {
            repository.store(file.getAbsolutePath(), caaExperimentData);
        } catch (Exception anyException) {
            ErrorAlert errorAlert = new ErrorAlert("Can not write caa experiment results from file", "Error occurred while witting to file", anyException.getMessage());
            errorAlert.showAndWait();
            throw new IllegalStateException(anyException);
        }
    }

    public CaaExperimentData loadExperimentData(File chosenFile) {
        try {
            return repository.read(chosenFile.getAbsolutePath());
        } catch (Exception anyException) {
            ErrorAlert errorAlert = new ErrorAlert("Can not load caa experiment results from file", "Invalid results file provided", anyException.getMessage());
            errorAlert.showAndWait();
            throw new IllegalStateException(anyException);
        }
    }
}
