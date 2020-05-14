package com.mtanevski.master.caa.gui.commands;

import com.mtanevski.master.caa.gui.MainController;
import com.mtanevski.master.caa.gui.SessionManager;
import com.mtanevski.master.caa.gui.statistics.GraphStatisticsController;
import com.mtanevski.master.caa.gui.utils.FxmlLoader;
import com.mtanevski.master.caa.gui.utils.StageUtils;
import javafx.scene.Parent;

import static com.mtanevski.master.caa.gui.DefaultValues.GRAPH_STATS_TITLE;

public class ViewGraphDetailsCommand extends Command {

    public ViewGraphDetailsCommand(MainController controller) {
        super(controller);
    }

    @Override
    public void invoke() {
        GraphStatisticsController graphStatisticsController = new GraphStatisticsController();
        Parent load = FxmlLoader.load("fxml/graph-statistics.fxml", graphStatisticsController);
        graphStatisticsController.setGraphData(SessionManager.graph());
        StageUtils.showModalStage(GRAPH_STATS_TITLE, controller.web.getParent().getScene().getWindow(), load);
    }

}
