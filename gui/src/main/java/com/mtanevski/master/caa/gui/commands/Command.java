package com.mtanevski.master.caa.gui.commands;

import com.mtanevski.master.caa.gui.MainController;

public abstract class Command {
    final MainController controller;

    public Command(MainController controller) {
        this.controller = controller;
    }

    public abstract void invoke();
}
