package com.mtanevski.master.caa.gui.controllers;

import com.mtanevski.master.caa.gui.alerts.ErrorAlert;
import com.mtanevski.master.caa.gui.events.VisJsLoadedEvent;
import com.mtanevski.master.caa.gui.web.VisJs;
import com.mtanevski.master.caa.lib.CaaGraph;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Objects;

import static javafx.concurrent.Worker.State.SUCCEEDED;

@Component
public class GraphVisualizerController {

    @FXML
    public Tab graphTab;
    @FXML
    public WebView web;

    private VisJs visJs;
    private final MessageSource messageSource;
    private final ApplicationContext context;

    public GraphVisualizerController(ApplicationContext applicationContext,
                                     MessageSource messageSource) {
        this.context = applicationContext;
        this.messageSource = messageSource;
    }

    @FXML
    public void initialize() {
        // TODO
        String html = Objects.requireNonNull(GraphVisualizerController.class.getClassLoader()
                .getResource("web/index.html"))
                .toExternalForm();

        WebEngine webEngine = this.web.getEngine();
        webEngine.load(html);

        // set up the listener
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (SUCCEEDED == newValue) {
                visJs = new VisJs((JSObject) webEngine.executeScript("window"));
                context.publishEvent(new VisJsLoadedEvent());
            }
        });
        webEngine.setOnAlert(eventData -> {
            String title = messageSource.getMessage("graph.visualization.alert.title",
                    null, Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setContentText(eventData.getData());

            alert.showAndWait();
        });
        graphTab.setGraphic(new ImageView(new Image("images/graph-icon.png")));
    }

    public VisJs getVisJs() {
        return visJs;
    }

    public void setGraphTitle(String title) {
        graphTab.setText(title);
    }

    public void visualizeGraph(CaaGraph graph, String title) {
        try {
            this.getVisJs().visualizeGraph(graph);
        } catch (Exception exception) {
            String errorTitle = messageSource.getMessage("graph.visualization.error.title", null, Locale.getDefault());
            String errorHeader = messageSource.getMessage("graph.visualization.error.header", null, Locale.getDefault());
            new ErrorAlert(errorTitle, errorHeader, exception.getMessage()).showAndWait();
            throw new IllegalStateException(exception);
        }
        graphTab.setText(title);
    }

}
