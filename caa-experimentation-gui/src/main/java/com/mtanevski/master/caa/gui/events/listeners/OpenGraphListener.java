package com.mtanevski.master.caa.gui.events.listeners;

import com.mtanevski.master.caa.gui.controllers.GraphVisualizerController;
import com.mtanevski.master.caa.gui.events.OpenGraphEvent;
import com.mtanevski.master.caa.gui.repositories.RecentFilesRepository;
import com.mtanevski.master.caa.gui.services.CaaSessionService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OpenGraphListener implements ApplicationListener<OpenGraphEvent> {
    private final RecentFilesRepository recentFilesRepository;
    private final GraphVisualizerController graphVisualizerController;
    private final CaaSessionService caaSessionService;

    public OpenGraphListener(RecentFilesRepository recentFilesRepository,
                             GraphVisualizerController graphVisualizerController,
                             CaaSessionService caaSessionService) {
        this.recentFilesRepository = recentFilesRepository;
        this.graphVisualizerController = graphVisualizerController;
        this.caaSessionService = caaSessionService;
    }

    @Override
    public void onApplicationEvent(OpenGraphEvent event) {
        try {
            caaSessionService.openGraph(event.getFile().getAbsolutePath());
            graphVisualizerController.visualizeGraph(
                    caaSessionService.getOpenGraph(),
                    caaSessionService.getOpenGraphFile().getName());
            recentFilesRepository.add(event.getFile().getAbsolutePath());
        } catch (Exception exc) {
            recentFilesRepository.remove(event.getFile().getAbsolutePath());
        }
    }

}
