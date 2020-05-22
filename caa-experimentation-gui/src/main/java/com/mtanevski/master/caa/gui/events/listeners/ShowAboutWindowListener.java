package com.mtanevski.master.caa.gui.events.listeners;

import com.mtanevski.master.caa.gui.alerts.InformationAlert;
import com.mtanevski.master.caa.gui.events.ShowAboutWindowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ShowAboutWindowListener implements ApplicationListener<ShowAboutWindowEvent> {

    @Autowired
    private final MessageSource messageSource;

    public ShowAboutWindowListener(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void onApplicationEvent(ShowAboutWindowEvent showAboutWindowEvent) {
        String title = messageSource.getMessage("about.title", null, Locale.getDefault());
        String header = messageSource.getMessage("about.header", null, Locale.getDefault());
        String content = messageSource.getMessage("about.content", null, Locale.getDefault());
        new InformationAlert(title, header, content).showAndWait();
    }
}
