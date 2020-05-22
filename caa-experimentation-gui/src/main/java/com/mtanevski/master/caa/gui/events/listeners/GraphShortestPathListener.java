package com.mtanevski.master.caa.gui.events.listeners;

import com.mtanevski.master.caa.gui.controllers.GraphVisualizerController;
import com.mtanevski.master.caa.gui.events.GraphShortestPathEvent;
import com.mtanevski.master.caa.gui.services.CaaSessionService;
import com.mtanevski.master.caa.lib.CaaGraph;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class GraphShortestPathListener implements ApplicationListener<GraphShortestPathEvent> {

    private final GraphVisualizerController graphVisualizerController;
    private final CaaSessionService caaSessionService;

    public GraphShortestPathListener(GraphVisualizerController graphVisualizerController,
                                     CaaSessionService caaSessionService) {
        this.graphVisualizerController = graphVisualizerController;
        this.caaSessionService = caaSessionService;
    }

    @Override
    public void onApplicationEvent(GraphShortestPathEvent event) {
        CaaGraph openGraph = caaSessionService.getOpenGraph();
        if (event.shouldMark()) {
            graphVisualizerController.getVisJs().visualizeShortestPaths(openGraph);
        } else {
            graphVisualizerController.getVisJs().removeVisualizationForShortestPaths(openGraph);
        }
    }
}
