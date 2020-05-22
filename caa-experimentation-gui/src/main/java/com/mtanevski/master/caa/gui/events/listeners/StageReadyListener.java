package com.mtanevski.master.caa.gui.events.listeners;

import com.mtanevski.master.caa.gui.controllers.GraphVisualizerController;
import com.mtanevski.master.caa.gui.controllers.LoadingToolbarController;
import com.mtanevski.master.caa.gui.events.StageReadyEvent;
import com.mtanevski.master.caa.gui.utils.FxmlSpringLoaderAdapter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import static com.mtanevski.master.caa.gui.DefaultValues.WINDOW_HEIGHT;
import static com.mtanevski.master.caa.gui.DefaultValues.WINDOW_WIDTH;

@Component
public class StageReadyListener implements ApplicationListener<StageReadyEvent> {

    private final Resource fxml;
    private final ApplicationContext context;
    private final LoadingToolbarController loadingToolbarController;
    private final GraphVisualizerController graphVisualizerController;
    private final String title;

    public StageReadyListener(
            @Value("${application.title}") String title,
            @Value("classpath:fxml/main.fxml") Resource fxml,
            ApplicationContext context,
            LoadingToolbarController loadingToolbarController,
            GraphVisualizerController graphVisualizerController) {
        this.title = title;
        this.fxml = fxml;
        this.context = context;
        this.loadingToolbarController = loadingToolbarController;
        this.graphVisualizerController = graphVisualizerController;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent stageReadyEvent) {
        Stage stage = stageReadyEvent.getStage();
        FXMLLoader loader = FxmlSpringLoaderAdapter.load(this.fxml, context);
        Parent root = loader.getRoot();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.setTitle(this.title);
        stage.show();

        loadingToolbarController.bindProgressBar(graphVisualizerController.web.getEngine().getLoadWorker().progressProperty());
    }
}
