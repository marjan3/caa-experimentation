package com.mtanevski.master.caa.gui.events.listeners;

import com.mtanevski.master.caa.gui.controllers.GraphVisualizerController;
import com.mtanevski.master.caa.gui.events.VisJsLoadedEvent;
import com.mtanevski.master.caa.gui.repositories.PreferencesRepository;
import com.mtanevski.master.caa.gui.services.CaaSessionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class VisJsLoadedListener implements ApplicationListener<VisJsLoadedEvent> {
    private final GraphVisualizerController graphVisualizerController;
    private final CaaSessionService caaSessionService;
    private final PreferencesRepository preferencesRepository;
    private final String sampleGraphLocation;

    public VisJsLoadedListener(GraphVisualizerController graphVisualizerController,
                               CaaSessionService caaSessionService,
                               PreferencesRepository preferencesRepository,
                               @Value("${sample.graph.location}") String sampleGraphLocation) {
        this.graphVisualizerController = graphVisualizerController;
        this.caaSessionService = caaSessionService;
        this.preferencesRepository = preferencesRepository;
        this.sampleGraphLocation = sampleGraphLocation;
    }

    @Override
    public void onApplicationEvent(VisJsLoadedEvent webPageLoadedEvent) {
        this.graphVisualizerController.getVisJs().setOptions(this.preferencesRepository.getPreferences());
        caaSessionService.openGraph(this.sampleGraphLocation);
        graphVisualizerController.visualizeGraph(
                caaSessionService.getOpenGraph(),
                caaSessionService.getOpenGraphFile().getName());
    }
}
