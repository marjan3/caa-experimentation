package com.mtanevski.master.caa.gui.commands;

import com.mtanevski.master.caa.gui.DefaultValues;
import com.mtanevski.master.caa.gui.alerts.InformationAlert;

public class ShowAboutDialogCommand {
    public void invoke() {
        InformationAlert alert = new InformationAlert(
                DefaultValues.ABOUT_ALERT_TITLE,
                DefaultValues.ABOUT_ALERT_HEADER,
                DefaultValues.ABOUT_CONTENT);
        alert.showAndWait();
    }
}
