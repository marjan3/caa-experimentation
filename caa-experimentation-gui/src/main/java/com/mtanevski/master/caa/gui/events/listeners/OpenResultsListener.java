package com.mtanevski.master.caa.gui.events.listeners;

import com.mtanevski.master.caa.gui.controllers.WorkspaceController;
import com.mtanevski.master.caa.gui.events.OpenResultsEvent;
import com.mtanevski.master.caa.gui.models.CaaExperimentData;
import com.mtanevski.master.caa.gui.repositories.RecentFilesRepository;
import com.mtanevski.master.caa.gui.services.CaaExperimentDataService;
import javafx.scene.control.Tab;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OpenResultsListener implements ApplicationListener<OpenResultsEvent> {

    private final RecentFilesRepository recentFilesRepository;
    private final WorkspaceController workspaceController;
    private final CaaExperimentDataService caaExperimentDataService;

    public OpenResultsListener(RecentFilesRepository recentFilesRepository,
                               WorkspaceController workspaceController,
                               CaaExperimentDataService caaExperimentDataService) {
        this.recentFilesRepository = recentFilesRepository;
        this.workspaceController = workspaceController;
        this.caaExperimentDataService = caaExperimentDataService;
    }

    @Override
    public void onApplicationEvent(OpenResultsEvent event) {
        try {
            CaaExperimentData caaExperimentData = caaExperimentDataService.loadExperimentData(event.getFile());
            Tab newTab = workspaceController.newResultsPane(caaExperimentData, event.getFile().getName());
            workspaceController.workspace.getTabs().add(newTab);
            recentFilesRepository.add(event.getFile().getAbsolutePath());
        } catch (Exception anyException) {
            recentFilesRepository.remove(event.getFile().getAbsolutePath());
        }
    }
}
