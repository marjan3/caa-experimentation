package com.mtanevski.master.caa.gui.controllers;

import com.mtanevski.master.caa.gui.models.CaaExperimentData;
import com.mtanevski.master.caa.gui.services.CaaSessionService;
import com.mtanevski.master.caa.gui.utils.FxmlSpringLoaderAdapter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class WorkspaceController {

    @FXML
    public TabPane workspace;

    private final ApplicationContext context;
    private final Resource fxml;
    private final CaaSessionService caaSessionService;
    private final MessageSource messageSource;
    private final TopMenuController topMenuController;

    public WorkspaceController(ApplicationContext context,
                               @Value("classpath:fxml/results.fxml") Resource fxml,
                               CaaSessionService caaSessionService,
                               MessageSource messageSource,
                               TopMenuController topMenuController) {
        this.context = context;
        this.fxml = fxml;
        this.caaSessionService = caaSessionService;
        this.messageSource = messageSource;
        this.topMenuController = topMenuController;
    }

    @FXML
    public void initialize() {
        workspace.getSelectionModel().selectedIndexProperty()
                .addListener((b, o, n) -> topMenuController.saveResultsItem.setDisable(n.intValue() == 0));
    }

    public void addWorkspace(String name, CaaExperimentData openExperiment) {
        String title = messageSource.getMessage("experiment.results.tab.title",
                new Object[]{name, workspace.getTabs().size()},
                Locale.getDefault());
        Tab newTab = this.newResultsPane(openExperiment, title);
        workspace.getTabs().add(newTab);
    }

    public Tab newResultsPane(CaaExperimentData experimentData, String title) {
        FXMLLoader loader = FxmlSpringLoaderAdapter.load(this.fxml, context);
        ExperimentResultsController controller = loader.getController();
        controller.setGraphData(experimentData);
        int id = this.workspace.getTabs().size();
        caaSessionService.setRememberedExperiment(id, experimentData);
        Tab resultsTab = new Tab();
        resultsTab.setClosable(true);
        resultsTab.setUserData(id);
        resultsTab.setText(title);
        resultsTab.setContent(loader.getRoot());
        return resultsTab;
    }

    public Integer getSelectedWorkspace() {
        Tab selectedTab = workspace.getSelectionModel().getSelectedItem();
        return (Integer) selectedTab.getUserData();
    }
}
