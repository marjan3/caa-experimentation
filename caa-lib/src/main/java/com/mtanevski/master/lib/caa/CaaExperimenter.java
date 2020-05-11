package com.mtanevski.master.lib.caa;

import com.mtanevski.master.lib.caa.impl.experiment.CaaExperimentData;

public interface CaaExperimenter {

    CaaExperimentData getData();

    CaaGraph getTraversedGraph();

    CaaGraph getInitialGraph();

    int executeExperiment(CaaAgent caaAgent);

    void executeTraversal(CaaAgent caaAgent);

    void resetTraversal();

    boolean pathToHappinessFound();

    boolean canTraverse();

    CaaEdge traverse(CaaAgent caaAgent);

    void trail(CaaEdge caaEdge);
}
