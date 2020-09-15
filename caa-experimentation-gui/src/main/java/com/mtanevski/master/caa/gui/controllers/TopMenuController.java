package com.mtanevski.master.caa.gui.controllers;

import com.mtanevski.master.caa.gui.alerts.FindHappyStateAlert;
import com.mtanevski.master.caa.gui.events.AnimateCaaAgentTraversalEvent;
import com.mtanevski.master.caa.gui.events.AnimateCaaExperimentEvent;
import com.mtanevski.master.caa.gui.events.GraphShortestPathEvent;
import com.mtanevski.master.caa.gui.events.OpenGraphEvent;
import com.mtanevski.master.caa.gui.events.OpenPreferencesEvent;
import com.mtanevski.master.caa.gui.events.OpenResultsEvent;
import com.mtanevski.master.caa.gui.events.SaveResultsEvent;
import com.mtanevski.master.caa.gui.events.ShowAboutWindowEvent;
import com.mtanevski.master.caa.gui.events.StageExitEvent;
import com.mtanevski.master.caa.gui.events.ViewGraphDetailsEvent;
import com.mtanevski.master.caa.gui.repositories.RecentFilesRepository;
import com.mtanevski.master.caa.gui.utils.RetentionFileChooser;
import com.mtanevski.master.caa.lib.impl.agents.CaaAgentType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Window;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TopMenuController {

    private final ApplicationContext context;
    private final RecentFilesRepository recentFilesRepository;

    @FXML
    public Menu openRecentMenu;

    @FXML
    public MenuItem saveResultsItem;

    @FXML
    public CheckMenuItem viewShortestPathsItem;

    public TopMenuController(ApplicationContext context,
                             RecentFilesRepository recentFilesRepository) {
        this.context = context;
        this.recentFilesRepository = recentFilesRepository;
    }

    @FXML
    public void initialize() {
        this.addAllRecentFilesToMenu();
    }

    @FXML
    public void openGraphFile(ActionEvent event) {
        Window window = ((MenuItem) event.getTarget()).getParentPopup().getOwnerWindow();
        File chosenGraphFile = RetentionFileChooser.showOpenGraphDialog(window);
        if (chosenGraphFile != null) {
            this.context.publishEvent(new OpenGraphEvent(chosenGraphFile));
            this.addAllRecentFilesToMenu();
            viewShortestPathsItem.setSelected(false);
        }
    }

    @FXML
    public void openResultsFile(ActionEvent event) {
        Window window = ((MenuItem) event.getTarget()).getParentPopup().getOwnerWindow();
        File chosenFile = RetentionFileChooser.showOpenResultsDialog(window);
        if (chosenFile != null) {
            this.context.publishEvent(new OpenResultsEvent(chosenFile));
            this.addAllRecentFilesToMenu();
        }
    }

    @FXML
    public void saveResults() {
        this.context.publishEvent(new SaveResultsEvent());
    }

    @FXML
    public void openPreferences() {
        this.context.publishEvent(new OpenPreferencesEvent());
    }

    @FXML
    public void animateCaaAgentTraversal() {
        FindHappyStateAlert caaAnimationAlert = new FindHappyStateAlert().withAgentSelection().withAnimateSelection();
        Optional<List<Object>> result = caaAnimationAlert.showAndWait();
        if (result.isPresent()) {
            List<Object> results = result.get();
            CaaAgentType caaAgentType = (CaaAgentType) results.get(0);
            Boolean shouldAnimate = (Boolean) results.get(1);
            this.context.publishEvent(new AnimateCaaAgentTraversalEvent(caaAgentType, shouldAnimate));
        }
    }

    @FXML
    public void animateCaaExperiment() {
        FindHappyStateAlert caaAnimationAlert =
                new FindHappyStateAlert()
                        .withIncrements()
                        .withAgentSelection()
                        .withAnimateSelection();
        Optional<List<Object>> result = caaAnimationAlert.showAndWait();
        if (!result.isPresent()) {
            return;
        }

        List<Object> results = result.get();
        Double sadMultiplier = (Double) results.get(1);
        if (sadMultiplier > 0) {
            sadMultiplier = sadMultiplier * -1;
        }
        Boolean shouldAnimate = (Boolean) results.get(3);
        Double happyMultiplier = (Double) results.get(0);
        CaaAgentType agent = (CaaAgentType) results.get(2);


        this.context.publishEvent(new AnimateCaaExperimentEvent(happyMultiplier, sadMultiplier, agent, shouldAnimate));

    }

    @FXML
    public void viewShortestPath(ActionEvent event) {
        CheckMenuItem source = (CheckMenuItem) event.getSource();
        this.context.publishEvent(new GraphShortestPathEvent(source.isSelected()));
    }

    @FXML
    public void viewGraphDetails() {
        this.context.publishEvent(new ViewGraphDetailsEvent());
    }

    @FXML
    public void about() {
        this.context.publishEvent(new ShowAboutWindowEvent());
    }

    @FXML
    public void exit() {
        this.context.publishEvent(new StageExitEvent());
    }

    public void addAllRecentFilesToMenu() {
        openRecentMenu.getItems().clear();
        openRecentMenu.getItems().addAll(
                this.recentFilesRepository.getAll()
                        .stream()
                        .map(toRecentFileMenu())
                        .collect(Collectors.toList()));
    }

    private Function<String, MenuItem> toRecentFileMenu() {
        return f -> {
            MenuItem menuItem = new MenuItem(f);
            File chosenFile = new File(f);
            menuItem.setOnAction(action -> {
                if (f.endsWith(".graphml")) {
                    this.context.publishEvent(new OpenGraphEvent(chosenFile));
                    viewShortestPathsItem.setSelected(false);
                } else if (f.endsWith(".json")) {
                    this.context.publishEvent(new OpenResultsEvent(chosenFile));
                }
            });
            return menuItem;
        };
    }

}
