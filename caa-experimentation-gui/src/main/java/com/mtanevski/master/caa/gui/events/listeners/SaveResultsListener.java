package com.mtanevski.master.caa.gui.events.listeners;

import com.mtanevski.master.caa.gui.controllers.WorkspaceController;
import com.mtanevski.master.caa.gui.events.SaveResultsEvent;
import com.mtanevski.master.caa.gui.services.CaaExperimentDataService;
import com.mtanevski.master.caa.gui.services.CaaSessionService;
import com.mtanevski.master.caa.gui.utils.RetentionFileChooser;
import com.mtanevski.master.caa.gui.DefaultValues;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class SaveResultsListener implements ApplicationListener<SaveResultsEvent> {

    private final WorkspaceController workspaceController;
    private final CaaExperimentDataService caaExperimentDataService;
    private final CaaSessionService caaSessionService;

    public SaveResultsListener(WorkspaceController workspaceController,
                               CaaExperimentDataService caaExperimentDataService,
                               CaaSessionService caaSessionService
    ) {
        this.workspaceController = workspaceController;
        this.caaExperimentDataService = caaExperimentDataService;
        this.caaSessionService = caaSessionService;
    }

    @Override
    public void onApplicationEvent(SaveResultsEvent saveResultsEvent) {
        Integer id = workspaceController.getSelectedWorkspace();
        String initialFileName = caaSessionService.getOpenGraphFile().getName() + DefaultValues.RESULTS_FILE_NAME_EXTENSION;
        File file = RetentionFileChooser.showSaveResultsDialog(initialFileName);
        if (file != null) {
            caaExperimentDataService.saveExperimentData(file, caaSessionService.getRememberedExperiment(id));
        }
    }
}
