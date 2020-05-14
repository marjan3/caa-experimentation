package com.mtanevski.master.caa.gui.commands;

import javafx.application.Platform;

public class ExitCommand {
    public void invoke() {
        Platform.exit();
    }
}
