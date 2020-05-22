package com.mtanevski.master.caa.gui.events.listeners;

import com.mtanevski.master.caa.gui.controllers.GraphVisualizerController;
import com.mtanevski.master.caa.gui.controllers.WorkspaceController;
import com.mtanevski.master.caa.gui.events.AnimateCaaExperimentEvent;
import com.mtanevski.master.caa.gui.models.CaaExperimentData;
import com.mtanevski.master.caa.gui.services.CaaSessionService;
import com.mtanevski.master.caa.lib.CaaGraph;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class AnimateCaaExperimentListener implements ApplicationListener<AnimateCaaExperimentEvent> {

    private final GraphVisualizerController graphVisualizerController;
    private final WorkspaceController workspaceController;
    private final CaaSessionService caaSessionService;

    public AnimateCaaExperimentListener(
            GraphVisualizerController graphVisualizerController,
            WorkspaceController workspaceController,
            CaaSessionService caaSessionService) {
        this.graphVisualizerController = graphVisualizerController;
        this.workspaceController = workspaceController;
        this.caaSessionService = caaSessionService;
    }

    @Override
    public void onApplicationEvent(AnimateCaaExperimentEvent event) {
        CaaGraph openGraph = caaSessionService.getOpenGraph();
        openGraph.resetWeightData();

        CaaExperimentData experimentData = caaSessionService.executeExperiment(
                event.getHappyMultiplier(),
                event.getSadMultiplier(),
                event.getAgent()
        );

        graphVisualizerController.getVisJs()
                .animateCaaExperiment(experimentData.getResults(), event.getShouldAnimate());
        workspaceController.addWorkspace(
                caaSessionService.getOpenGraphFile().getName(),
                caaSessionService.getExecutedExperiment());
    }
}
