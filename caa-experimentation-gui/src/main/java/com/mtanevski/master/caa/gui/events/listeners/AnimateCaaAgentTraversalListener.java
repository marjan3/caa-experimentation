package com.mtanevski.master.caa.gui.events.listeners;

import com.mtanevski.master.caa.gui.controllers.GraphVisualizerController;
import com.mtanevski.master.caa.gui.events.AnimateCaaAgentTraversalEvent;
import com.mtanevski.master.caa.gui.services.CaaSessionService;
import com.mtanevski.master.caa.lib.CaaAgent;
import com.mtanevski.master.caa.lib.CaaEdge;
import com.mtanevski.master.caa.lib.CaaExperimentResults;
import com.mtanevski.master.caa.lib.CaaExperimenter;
import com.mtanevski.master.caa.lib.CaaGraph;
import com.mtanevski.master.caa.lib.impl.agents.CaaAgentFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
public class AnimateCaaAgentTraversalListener implements ApplicationListener<AnimateCaaAgentTraversalEvent> {

    private final GraphVisualizerController graphVisualizerController;
    private final CaaSessionService caaSessionService;

    public AnimateCaaAgentTraversalListener(GraphVisualizerController graphVisualizerController,
                                            CaaSessionService caaSessionService) {
        this.graphVisualizerController = graphVisualizerController;
        this.caaSessionService = caaSessionService;
    }

    @Override
    public void onApplicationEvent(AnimateCaaAgentTraversalEvent event) {
        CaaGraph openGraph = caaSessionService.getOpenGraph();
        CaaAgent agent = CaaAgentFactory.getAgent(event.getCaaAgentType());
        Iterator<CaaEdge> iterator = openGraph.iterator(agent);
        CaaExperimentResults results = new CaaExperimentResults(openGraph.getStart());
        CaaExperimenter caaExperimenter = new CaaExperimenter();
        while (iterator.hasNext()) {
            CaaEdge edge = iterator.next();
            caaExperimenter.trailEdge(edge, openGraph, results);
        }
        graphVisualizerController.getVisJs().animateCaaVertices(results, event.shouldAnimate());
    }
}
