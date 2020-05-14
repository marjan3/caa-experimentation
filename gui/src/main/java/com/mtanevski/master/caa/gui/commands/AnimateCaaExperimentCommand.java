package com.mtanevski.master.caa.gui.commands;

import com.mtanevski.master.caa.gui.MainController;
import com.mtanevski.master.caa.gui.SessionManager;
import com.mtanevski.master.caa.gui.alerts.FindHappyStateAlert;
import com.mtanevski.master.caa.lib.impl.agents.CaaAgentType;
import javafx.scene.control.Tab;

import java.util.List;
import java.util.Optional;

public class AnimateCaaExperimentCommand extends Command {

    public AnimateCaaExperimentCommand(MainController controller) {
        super(controller);
    }

    @Override
    public void invoke() {
        FindHappyStateAlert caaAnimationAlert =
                new FindHappyStateAlert()
                        .withIncrements()
                        .withAgentSelection()
                        .withAnimateSelection();
        Optional<List<Object>> result = caaAnimationAlert.showAndWait();
        if (result.isPresent()) {
            List<Object> results = result.get();
            Double sadMultiplier = (Double) results.get(1);
            if (sadMultiplier > 0) {
                sadMultiplier = sadMultiplier * -1;
            }
            Boolean shouldAnimate = (Boolean) results.get(3);
            Double happyMultiplier = (Double) results.get(0);
            CaaAgentType agent = (CaaAgentType) results.get(2);
            controller.webWrapper.visualizeCaaExperiment(happyMultiplier, sadMultiplier, agent, shouldAnimate);

            String title = "Results (" + SessionManager.graphFile().getName() + ") #" + controller.workspace.getTabs().size();
            Tab newTab = controller.newResultsPane(SessionManager.experiment(), title);
            controller.workspace.getTabs().add(newTab);
        }
    }

}
