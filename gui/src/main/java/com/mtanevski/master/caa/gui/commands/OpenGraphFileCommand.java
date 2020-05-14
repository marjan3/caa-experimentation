package com.mtanevski.master.caa.gui.commands;

import com.mtanevski.master.caa.gui.MainController;
import com.mtanevski.master.caa.gui.SessionManager;
import com.mtanevski.master.caa.gui.utils.RetentionFileChooser;

import java.io.File;

public class OpenGraphFileCommand extends Command {

    public OpenGraphFileCommand(MainController controller) {
        super(controller);
    }

    @Override
    public void invoke() {
        File chosenGraphFile = RetentionFileChooser.showOpenGraphDialog(controller.web.getParent().getScene().getWindow());
        if (chosenGraphFile != null) {
            controller.addRecentFilesToOpenRecentMenu();
            SessionManager.loadGraph(chosenGraphFile.getAbsolutePath());
            controller.webWrapper.visualizeGraph(SessionManager.graph());
            controller.graphTab.setText(SessionManager.graphFile().getName());
        }
    }

}
