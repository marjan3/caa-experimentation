package com.mtanevski.master.caa.gui.commands;

import com.mtanevski.master.caa.gui.MainController;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckMenuItem;

public class ViewShortestPathCommand extends Command {
    private final ActionEvent event;

    public ViewShortestPathCommand(ActionEvent event, MainController mainController) {
        super(mainController);
        this.event = event;
    }

    @Override
    public void invoke() {
        CheckMenuItem source = (CheckMenuItem) event.getSource();
        if (source.isSelected()) {
            controller.webWrapper.markShortestPaths();
        } else {
            controller.webWrapper.removeShortestPaths();
        }
    }

}
