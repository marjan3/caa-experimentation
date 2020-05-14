package com.mtanevski.master.caa.gui.commands;

import com.mtanevski.master.caa.gui.MainController;
import com.mtanevski.master.caa.gui.alerts.FindHappyStateAlert;
import com.mtanevski.master.caa.lib.impl.agents.CaaAgentType;

import java.util.List;
import java.util.Optional;

public class AnimateCaaAgentTraversalCommand extends Command {

    public AnimateCaaAgentTraversalCommand(MainController controller) {
        super(controller);
    }

    @Override
    public void invoke() {
        FindHappyStateAlert caaAnimationAlert = new FindHappyStateAlert().withAgentSelection().withAnimateSelection();
        Optional<List<Object>> result = caaAnimationAlert.showAndWait();
        if (result.isPresent()) {
            List<Object> results = result.get();
            CaaAgentType caaAgentType = (CaaAgentType) results.get(0);
            Boolean shouldAnimate = (Boolean) results.get(1);
            controller.webWrapper.visualizeCaaAlgorithmTraversal(caaAgentType, shouldAnimate);
        }
    }

}
