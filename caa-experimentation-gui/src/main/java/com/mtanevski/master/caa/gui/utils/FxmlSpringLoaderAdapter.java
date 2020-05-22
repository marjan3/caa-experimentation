package com.mtanevski.master.caa.gui.utils;

import javafx.fxml.FXMLLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceResourceBundle;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Locale;

public class FxmlSpringLoaderAdapter {

    public static FXMLLoader load(Resource fxmlResourceLocation, ApplicationContext context) {
        FXMLLoader loader;
        try {
            loader = new FXMLLoader(fxmlResourceLocation.getURL());
            loader.setControllerFactory(context::getBean);
            MessageSourceResourceBundle messageSource
                    = new MessageSourceResourceBundle(
                    context.getBean(MessageSource.class), Locale.getDefault());
            loader.setResources(messageSource);
            loader.load();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
        return loader;
    }

}
