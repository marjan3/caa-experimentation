package com.mtanevski.master.caa.gui.commands;

import com.mtanevski.master.caa.gui.DefaultValues;
import com.mtanevski.master.caa.gui.MainController;
import com.mtanevski.master.caa.gui.preferences.PreferencesController;
import com.mtanevski.master.caa.gui.utils.FxmlLoader;
import com.mtanevski.master.caa.gui.utils.StageUtils;
import com.mtanevski.master.caa.gui.visualization.VisOptions;
import javafx.scene.Parent;

public class OpenPreferencesCommand extends Command {

    public OpenPreferencesCommand(MainController controller) {
        super(controller);
    }

    @Override
    public void invoke() {
        PreferencesController preferencesController = new PreferencesController();
        Parent load = FxmlLoader.load("fxml/preferences.fxml", preferencesController);
        StageUtils.showModalStage(DefaultValues.PREFERENCES_TITLE, controller.web.getParent().getScene().getWindow(), load);

        preferencesController.verticesColor.valueProperty().addListener((b, o, n) -> controller.webWrapper.setOption("nodes.color", VisOptions.toWeb(n)));
        preferencesController.edgesColor.valueProperty().addListener((b, o, n) -> controller.webWrapper.setOption("edges.color.color", VisOptions.toWeb(n)));
        preferencesController.verticesLabels.selectedProperty().addListener((b, o, n) -> controller.webWrapper.setOption("nodes.font.size", VisOptions.toNodeFontSize(n)));
        preferencesController.edgesLabels.selectedProperty().addListener((b, o, n) -> controller.webWrapper.setOption("edges.font.size", VisOptions.toEdgeFontSize(n)));
        preferencesController.edgesWidth.valueProperty().addListener((b, o, n) -> controller.webWrapper.setOption("edges.width", n));
        preferencesController.shortestPathColor.valueProperty().addListener((b, o, n) -> controller.webWrapper.setOption("edges.background.color", VisOptions.toWeb(n)));

        preferencesController.startingVertexColor.valueProperty().addListener((b, o, n) -> controller.webWrapper.setStartVisNodeColor(n));
        preferencesController.happyVertexColor.valueProperty().addListener((b, o, n) -> controller.webWrapper.setHappyVisNodesColor(n));
        preferencesController.sadVertexColor.valueProperty().addListener((b, o, n) -> controller.webWrapper.setSadVisNodesColor(n));
    }

}
