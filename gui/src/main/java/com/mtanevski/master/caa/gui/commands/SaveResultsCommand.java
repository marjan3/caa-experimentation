package com.mtanevski.master.caa.gui.commands;

import com.mtanevski.master.caa.gui.MainController;
import com.mtanevski.master.caa.gui.SessionManager;
import com.mtanevski.master.caa.gui.utils.RetentionFileChooser;
import javafx.scene.control.Tab;

import java.io.File;

public class SaveResultsCommand extends Command {

    public SaveResultsCommand(MainController controller) {
        super(controller);
    }

    @Override
    public void invoke() {
        Tab selectedTab = controller.workspace.getSelectionModel().getSelectedItem();
        Integer id = (Integer) (selectedTab.getUserData());
        String initialFileName = SessionManager.graphFile().getName() + ".caa-experiment-results.json";
        File file = RetentionFileChooser.showSaveResultsDialog(initialFileName, controller.web.getParent().getScene().getWindow());
        if (file != null) {
            SessionManager.saveData(file, SessionManager.getRememberedExperiment(id));
        }
    }

}
