package com.mtanevski.master.fxml;

import com.mtanevski.master.fxml.alerts.FindHappyStateAlert;
import com.mtanevski.master.fxml.alerts.InformationAlert;
import com.mtanevski.master.fxml.preferences.PreferencesController;
import com.mtanevski.master.fxml.statistics.ExperimentsStatisticsController;
import com.mtanevski.master.fxml.statistics.GraphStatisticsController;
import com.mtanevski.master.fxml.utils.FxmlLoader;
import com.mtanevski.master.fxml.utils.RetentionFileChooser;
import com.mtanevski.master.fxml.utils.SessionManager;
import com.mtanevski.master.fxml.utils.StageUtils;
import com.mtanevski.master.fxml.visualization.VisOptions;
import com.mtanevski.master.fxml.visualization.WebWrapper;
import com.mtanevski.master.lib.caa.CaaExperimenter;
import com.mtanevski.master.lib.caa.impl.agents.CaaAgentFactory;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mtanevski.master.fxml.DefaultValues.*;

public class MainController {

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

    private WebWrapper webWrapper;

    @FXML
    public void initialize() {
        addRecentFilesToOpenRecentMenu();
        bootstrapProgressBar(web.getEngine().getLoadWorker().progressProperty());
        SessionManager.loadGraph(SAMPLE_GRAPH_LOCATION);
        webWrapper = new WebWrapper(this.web);
        graphTab.setText(SessionManager.graphFile().getName());
        this.saveResultsItem.setDisable(true);
        workspace.getSelectionModel().selectedIndexProperty().addListener((b, o, n) -> {
            if(n.intValue() == 0) {
                this.saveResultsItem.setDisable(true);
            } else {
                this.saveResultsItem.setDisable(false);

            }
        });

        // do not show and await -> that will slow down loading of visualization page
//        new LoadingAlert("Loading BasicGraph...", webEngine.getLoadWorker().progressProperty()).show();
    }

    @FXML
    public void openGraphFile() {
        File chosenGraphFile = RetentionFileChooser.showOpenGraphDialog(web.getParent().getScene().getWindow());
        if (chosenGraphFile != null) {
            addRecentFilesToOpenRecentMenu();
            SessionManager.loadGraph(chosenGraphFile.getAbsolutePath());
            webWrapper.visualizeGraph(SessionManager.graph());
            graphTab.setText(SessionManager.graphFile().getName());
        }
    }

    @FXML
    public void openResultsFile(ActionEvent event) {
        File chosenFile = RetentionFileChooser.showOpenResultsDialog(web.getParent().getScene().getWindow());
        if (chosenFile != null) {

            CaaExperimenter caaExperimenter = SessionManager.loadCaaExperimenterFromFile(chosenFile);
            Tab newTab = newResultsPane(caaExperimenter, chosenFile.getName());
            workspace.getTabs().add(newTab);
        }
    }

    @FXML
    public void saveResults() {
        Tab selectedTab = workspace.getSelectionModel().getSelectedItem();
        Integer id = (Integer)(selectedTab.getUserData());
        String initialFileName = SessionManager.graphFile().getName() + ".experiment-results.json";
        File file = RetentionFileChooser.showSaveResultsDialog(initialFileName, web.getParent().getScene().getWindow());
        if (file != null) {
            SessionManager.saveData(file, SessionManager.getRememberedExperiment(id));
        }
    }

    @FXML
    public void openPreferences() {
        PreferencesController controller = new PreferencesController();
        Parent load = FxmlLoader.load("fxml/preferences.fxml", controller);
        StageUtils.showModalStage(PREFERENCES_TITLE, this.web.getParent().getScene().getWindow(), load);

        controller.verticesColor.valueProperty().addListener((b, o, n) -> this.webWrapper.setOption("nodes.color", VisOptions.toWeb(n)));
        controller.edgesColor.valueProperty().addListener((b, o, n) -> this.webWrapper.setOption("edges.color.color", VisOptions.toWeb(n)));
        controller.verticesLabels.selectedProperty().addListener((b, o, n) -> this.webWrapper.setOption("nodes.font.size", VisOptions.toNodeFontSize(n)));
        controller.edgesLabels.selectedProperty().addListener((b, o, n) -> this.webWrapper.setOption("edges.font.size", VisOptions.toEdgeFontSize(n)));
        controller.edgesWidth.valueProperty().addListener((b, o, n) -> this.webWrapper.setOption("edges.width", n));
        controller.shortestPathColor.valueProperty().addListener((b, o, n) -> this.webWrapper.setOption("edges.background.color", VisOptions.toWeb(n)));

        controller.startingVertexColor.valueProperty().addListener((b, o, n) -> this.webWrapper.setStartVisNodeColor(n));
        controller.happyVertexColor.valueProperty().addListener((b, o, n) -> this.webWrapper.setHappyVisNodesColor(n));
        controller.sadVertexColor.valueProperty().addListener((b, o, n) -> this.webWrapper.setSadVisNodesColor(n));
    }

    @FXML
    public void animateCaaAgentTraversal() {
        FindHappyStateAlert caaAnimationAlert = new FindHappyStateAlert().withAgentSelection().withAnimateSelection();
        Optional<List<Object>> result = caaAnimationAlert.showAndWait();
        if (result.isPresent()) {
            List<Object> results = result.get();
            CaaAgentFactory.CaaAgentType caaAgentType = (CaaAgentFactory.CaaAgentType) results.get(0);
            Boolean shouldAnimate = (Boolean)results.get(1);
            this.webWrapper.visualizeCaaAlgorithmTraversal(caaAgentType, shouldAnimate);
        }
    }

    @FXML
    public void animateCaaExperiment() {
        FindHappyStateAlert caaAnimationAlert = new FindHappyStateAlert().withIncrements().withAgentSelection().withAnimateSelection();
        Optional<List<Object>> result = caaAnimationAlert.showAndWait();
        if (result.isPresent()) {
            List<Object> results = result.get();
            Double sadMultiplier = (Double)results.get(1);
            if (sadMultiplier > 0) {
                sadMultiplier = sadMultiplier * -1;
            }
            Boolean shouldAnimate = (Boolean) results.get(3);
            Double happyMultiplier = (Double) results.get(0);
            CaaAgentFactory.CaaAgentType agent = (CaaAgentFactory.CaaAgentType) results.get(2);
            this.webWrapper.visualizeCaaExperiment(happyMultiplier, sadMultiplier, agent, shouldAnimate);

            Tab newTab = newResultsPane(SessionManager.experimenter(), "Results (" + SessionManager.graphFile().getName() + ") #" + this.workspace.getTabs().size());
            workspace.getTabs().add(newTab);
        }
    }

    @FXML
    public void viewShortestPath(ActionEvent event) {
        CheckMenuItem source = (CheckMenuItem) event.getSource();
        if(source.isSelected()) {
            this.webWrapper.markShortestPaths();
        } else {
            this.webWrapper.removeShortestPaths();
        }
    }

    @FXML
    public void viewGraphDetails() {
        GraphStatisticsController controller = new GraphStatisticsController();
        Parent load = FxmlLoader.load("fxml/graph-statistics.fxml", controller);
        controller.setGraphData(SessionManager.graph());
        StageUtils.showModalStage(GRAPH_STATS_TITLE, this.web.getParent().getScene().getWindow(), load);
    }

    @FXML
    public void about() {
        InformationAlert alert = new InformationAlert(
                ABOUT_ALERT_TITLE,
                ABOUT_ALERT_HEADER,
                ABOUT_CONTENT);
        alert.showAndWait();
    }

    @FXML
    public void exit() {
        Platform.exit();
    }

    private Tab newResultsPane(CaaExperimenter caaExperimenter, String title){
        ExperimentsStatisticsController controller = new ExperimentsStatisticsController();
        AnchorPane loaded = FxmlLoader.load("fxml/results.fxml", controller);
        controller.setGraphData(caaExperimenter);
        int id = this.workspace.getTabs().size();
        SessionManager.rememberExperimenter(id, caaExperimenter);
        Tab resultsTab = new Tab();
        resultsTab.setClosable(true);
        resultsTab.setUserData(id);
        resultsTab.setText( title);
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

    private void addRecentFilesToOpenRecentMenu() {
        openRecentMenu.getItems().clear();
        openRecentMenu.getItems().addAll(RetentionFileChooser.getRecentFiles().stream().map(f -> {
            MenuItem menuItem = new MenuItem(f);
            menuItem.setOnAction(action -> {
                if(f.endsWith(".graphml")) {
                    boolean success = SessionManager.loadGraph(f);
                    this.webWrapper.visualizeGraph(SessionManager.graph());
                    if (!success) {
                        openRecentMenu.getItems().remove(menuItem);
                        RetentionFileChooser.removeFromRecentFiles(f);
                    }
                } else if (f.endsWith(".json")){
                    File chosenFile = new File(f);
                    try {
                        CaaExperimenter caaExperimenter = SessionManager.loadCaaExperimenterFromFile(chosenFile);
                        Tab newTab = newResultsPane(caaExperimenter, chosenFile.getName());
                        workspace.getTabs().add(newTab);
                    }catch (Exception anyException){
                        openRecentMenu.getItems().remove(menuItem);
                        RetentionFileChooser.removeFromRecentFiles(f);

                    }
                }
            });
            return menuItem;
        }).collect(Collectors.toList()));
    }
}
