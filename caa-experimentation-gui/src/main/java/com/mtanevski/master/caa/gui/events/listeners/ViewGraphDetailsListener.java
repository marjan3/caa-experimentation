package com.mtanevski.master.caa.gui.events.listeners;

import com.mtanevski.master.caa.gui.controllers.GraphDetailsController;
import com.mtanevski.master.caa.gui.events.ViewGraphDetailsEvent;
import com.mtanevski.master.caa.gui.repositories.PreferencesRepository;
import com.mtanevski.master.caa.gui.services.CaaSessionService;
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
public class ViewGraphDetailsListener implements ApplicationListener<ViewGraphDetailsEvent> {

    private final MessageSource messageSource;
    private final Resource fxml;
    private final PreferencesRepository preferencesRepository;
    private final CaaSessionService caaSessionService;
    private final ApplicationContext context;

    public ViewGraphDetailsListener(MessageSource messageSource,
                                    @Value("classpath:fxml/graph-details.fxml") Resource fxml,
                                    PreferencesRepository preferencesRepository,
                                    CaaSessionService caaSessionService,
                                    ApplicationContext context) {
        this.messageSource = messageSource;
        this.fxml = fxml;
        this.preferencesRepository = preferencesRepository;
        this.caaSessionService = caaSessionService;
        this.context = context;
    }

    @Override
    public void onApplicationEvent(ViewGraphDetailsEvent event) {
        FXMLLoader loader = FxmlSpringLoaderAdapter.load(fxml, context);
        GraphDetailsController controller = loader.getController();
        controller.setGraphData(caaSessionService.getOpenGraph(), preferencesRepository);
        String title = this.messageSource.getMessage("graph.details.title", null, Locale.getDefault());
        StageUtils.showModalStage(title, loader.getRoot());
    }

}
