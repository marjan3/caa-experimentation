package com.mtanevski.master.caa.gui;

import com.mtanevski.master.caa.gui.commands.AnimateCaaAgentTraversalCommand;
import com.mtanevski.master.caa.gui.commands.AnimateCaaExperimentCommand;
import com.mtanevski.master.caa.gui.commands.Command;
import com.mtanevski.master.caa.gui.commands.ExitCommand;
import com.mtanevski.master.caa.gui.commands.OpenGraphFileCommand;
import com.mtanevski.master.caa.gui.commands.OpenPreferencesCommand;
import com.mtanevski.master.caa.gui.commands.OpenResultsFileCommand;
import com.mtanevski.master.caa.gui.commands.SaveResultsCommand;
import com.mtanevski.master.caa.gui.commands.ShowAboutDialogCommand;
import com.mtanevski.master.caa.gui.commands.ViewGraphDetailsCommand;
import com.mtanevski.master.caa.gui.commands.ViewShortestPathCommand;
import com.mtanevski.master.caa.gui.statistics.ExperimentsStatisticsController;
import com.mtanevski.master.caa.gui.utils.FxmlLoader;
import com.mtanevski.master.caa.gui.utils.RetentionFileChooser;
import com.mtanevski.master.caa.gui.visualization.WebWrapper;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

import java.io.File;
import java.util.Stack;
import java.util.stream.Collectors;

public class MainController {

    private final Stack<Command> history = new Stack<>();
    @FXML
    public WebView web;
    @FXML
    public TabPane workspace;
    @FXML
    public Tab graphTab;
    @FXML
    public ProgressBar progressBar;
    @FXML
    public Label loadingLbl;
    @FXML
    public Menu openRecentMenu;
    @FXML
    public MenuItem saveResultsItem;
    public WebWrapper webWrapper;

    @FXML
    public void initialize() {
        addRecentFilesToOpenRecentMenu();
        bootstrapProgressBar(web.getEngine().getLoadWorker().progressProperty());
        SessionManager.loadGraph(DefaultValues.SAMPLE_GRAPH_LOCATION);
        webWrapper = new WebWrapper(this.web);
        graphTab.setText(SessionManager.graphFile().getName());
        this.saveResultsItem.setDisable(true);
        workspace.getSelectionModel().selectedIndexProperty()
                .addListener((b, o, n) -> this.saveResultsItem.setDisable(n.intValue() == 0));

        // do not show and await -> that will slow down loading of visualization page
//        new LoadingAlert("Loading BasicGraph...", webEngine.getLoadWorker().progressProperty()).show();
    }

    @FXML
    public void openGraphFile() {
        invokeCommand(new OpenGraphFileCommand(this));
    }

    @FXML
    public void openResultsFile(ActionEvent event) {
        invokeCommand(new OpenResultsFileCommand(this));
    }

    @FXML
    public void saveResults() {
        invokeCommand(new SaveResultsCommand(this));
    }

    @FXML
    public void openPreferences() {
        invokeCommand(new OpenPreferencesCommand(this));
    }

    @FXML
    public void animateCaaAgentTraversal() {
        invokeCommand(new AnimateCaaAgentTraversalCommand(this));
    }

    @FXML
    public void animateCaaExperiment() {
        invokeCommand(new AnimateCaaExperimentCommand(this));
    }

    @FXML
    public void viewShortestPath(ActionEvent event) {
        invokeCommand(new ViewShortestPathCommand(event, this));
    }

    @FXML
    public void viewGraphDetails() {
        invokeCommand(new ViewGraphDetailsCommand(this));
    }

    @FXML
    public void about() {
        new ShowAboutDialogCommand().invoke();
    }

    @FXML
    public void exit() {
        new ExitCommand().invoke();
    }

    private void invokeCommand(Command command) {
        command.invoke();
        history.push(command);
    }

    public Tab newResultsPane(CaaExperimentSaveData experimentData, String title) {
        ExperimentsStatisticsController controller = new ExperimentsStatisticsController();
        AnchorPane loaded = FxmlLoader.load("fxml/results.fxml", controller);
        controller.setGraphData(experimentData);
        int id = this.workspace.getTabs().size();
        SessionManager.rememberExperiment(id, experimentData);
        Tab resultsTab = new Tab();
        resultsTab.setClosable(true);
        resultsTab.setUserData(id);
        resultsTab.setText(title);
        resultsTab.setContent(loaded);

        return resultsTab;
    }

    private void bootstrapProgressBar(ReadOnlyDoubleProperty property) {
        this.loadingLbl.setLabelFor(this.progressBar);
        progressBar.progressProperty().bind(property);
        progressBar.progressProperty().addListener((o, oldProp, newProp) -> {
            if (newProp.doubleValue() == 1.0) {
                this.progressBar.setVisible(false);
                this.loadingLbl.setVisible(false);
            }
        });
    }

    public void addRecentFilesToOpenRecentMenu() {
        openRecentMenu.getItems().clear();
        openRecentMenu.getItems().addAll(RetentionFileChooser.getRecentFiles().stream().map(f -> {
            MenuItem menuItem = new MenuItem(f);
            menuItem.setOnAction(action -> {
                if (f.endsWith(".graphml")) {
                    boolean success = SessionManager.loadGraph(f);
                    this.webWrapper.visualizeGraph(SessionManager.graph());
                    if (!success) {
                        openRecentMenu.getItems().remove(menuItem);
                        RetentionFileChooser.removeFromRecentFiles(f);
                    }
                } else if (f.endsWith(".json")) {
                    File chosenFile = new File(f);
                    try {
                        CaaExperimentSaveData caaExperimentSaveData = SessionManager.loadCaaExperimenterFromFile(chosenFile);
                        Tab newTab = newResultsPane(caaExperimentSaveData, chosenFile.getName());
                        workspace.getTabs().add(newTab);
                    } catch (Exception anyException) {
                        openRecentMenu.getItems().remove(menuItem);
                        RetentionFileChooser.removeFromRecentFiles(f);

                    }
                }
            });
            return menuItem;
        }).collect(Collectors.toList()));
    }
}
