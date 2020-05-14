package com.mtanevski.master.caa.gui.commands;

import com.mtanevski.master.caa.gui.CaaExperimentSaveData;
import com.mtanevski.master.caa.gui.MainController;
import com.mtanevski.master.caa.gui.SessionManager;
import com.mtanevski.master.caa.gui.utils.RetentionFileChooser;
import javafx.scene.control.Tab;

import java.io.File;

public class OpenResultsFileCommand extends Command {

    public OpenResultsFileCommand(MainController mainController) {
        super(mainController);
    }

    @Override
    public void invoke() {
        File chosenFile = RetentionFileChooser.showOpenResultsDialog(controller.web.getParent().getScene().getWindow());
        if (chosenFile != null) {

            CaaExperimentSaveData caaExperimenterData = SessionManager.loadCaaExperimenterFromFile(chosenFile);
            Tab newTab = controller.newResultsPane(caaExperimenterData, chosenFile.getName());
            controller.workspace.getTabs().add(newTab);
        }
    }

}
