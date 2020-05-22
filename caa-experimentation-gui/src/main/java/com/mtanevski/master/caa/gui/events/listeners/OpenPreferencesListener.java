package com.mtanevski.master.caa.gui.events.listeners;

import com.mtanevski.master.caa.gui.controllers.GraphVisualizerController;
import com.mtanevski.master.caa.gui.controllers.PreferencesController;
import com.mtanevski.master.caa.gui.events.OpenPreferencesEvent;
import com.mtanevski.master.caa.gui.utils.FxmlSpringLoaderAdapter;
import com.mtanevski.master.caa.gui.utils.StageUtils;
import javafx.fxml.FXMLLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class OpenPreferencesListener implements ApplicationListener<OpenPreferencesEvent> {

    private final Resource fxml;
    private final GraphVisualizerController graphVisualizerController;
    private final ApplicationContext context;
    private MessageSource messageSource;

    public OpenPreferencesListener(
            @Value("classpath:fxml/preferences.fxml") Resource fxml,
            GraphVisualizerController graphVisualizerController,
            ApplicationContext context,
            MessageSource messageSource) {
        this.fxml = fxml;
        this.graphVisualizerController = graphVisualizerController;
        this.context = context;
        this.messageSource = messageSource;
    }

    @Override
    public void onApplicationEvent(OpenPreferencesEvent openPreferencesEvent) {
        FXMLLoader loader = FxmlSpringLoaderAdapter.load(this.fxml, context);
        String title = messageSource.getMessage("preferences.dialog.title", null, Locale.getDefault());
        StageUtils.showModalStage(title, loader.getRoot());
        PreferencesController preferencesController = loader.getController();

        preferencesController.verticesColor.valueProperty().addListener((b, o, n) ->
                graphVisualizerController.getVisJs().setVerticesColor(n));
        preferencesController.edgesColor.valueProperty().addListener((b, o, n) ->
                graphVisualizerController.getVisJs().setEdgesColor(n));
        preferencesController.verticesLabels.selectedProperty().addListener((b, o, n) ->
                graphVisualizerController.getVisJs().setShowVerticesLabels(n));
        preferencesController.edgesLabels.selectedProperty().addListener((b, o, n) ->
                graphVisualizerController.getVisJs().setShowEdgesLabels(n));
        preferencesController.edgesWidth.valueProperty().addListener(
                (b, o, n) -> graphVisualizerController.getVisJs().setEdgesWidth(n));
        preferencesController.shortestPathColor.valueProperty().addListener(
                (b, o, n) -> graphVisualizerController.getVisJs().setShortestPathColor(n));

        preferencesController.startingVertexColor.valueProperty().addListener((b, o, n) ->
                graphVisualizerController.getVisJs().setStartVisNodeColor(n));
        preferencesController.happyVertexColor.valueProperty().addListener((b, o, n) ->
                graphVisualizerController.getVisJs().setHappyVisNodesColor(n));
        preferencesController.sadVertexColor.valueProperty().addListener((b, o, n) ->
                graphVisualizerController.getVisJs().setSadVisNodesColor(n));
    }
}
