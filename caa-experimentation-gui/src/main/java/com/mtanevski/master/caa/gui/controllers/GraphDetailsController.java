package com.mtanevski.master.caa.gui.controllers;

import com.mtanevski.master.caa.gui.repositories.PreferencesRepository;
import com.mtanevski.master.caa.gui.web.WebUtils;
import com.mtanevski.master.caa.lib.CaaGraph;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class GraphDetailsController {

    @FXML
    public TextField startingVertexText;
    @FXML
    public TextField happyVerticesText;
    @FXML
    public TextField sadVerticesText;
    @FXML
    public TextField verticesText;
    @FXML
    public Parent edgesTableView;
    @FXML
    public EdgesTableController edgesTableViewController; // $IncludedView;+Controller
    @FXML
    public PieChart verticesTypesChart;

    private final MessageSource messageSource;

    public GraphDetailsController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setGraphData(CaaGraph caaGraph, PreferencesRepository preferencesRepository) {
        this.startingVertexText.setText(caaGraph.getStart());
        this.happyVerticesText.setText(StringUtils.join(caaGraph.getHappy(), ","));
        this.sadVerticesText.setText(StringUtils.join(caaGraph.getSad(), ","));
        this.verticesText.setText(StringUtils.join(caaGraph.getAllVertices(), ","));
        edgesTableViewController.setTableData(caaGraph);

        List<String> otherVertices = caaGraph.getAllVertices().stream()
                .filter(v -> !caaGraph.getSad().contains(v) && !caaGraph.getHappy().contains(v)).collect(Collectors.toList());
        String happyDataTitle = messageSource.getMessage("graph.details.chart.data.happy",
                new Object[]{caaGraph.getHappy().size()}, Locale.getDefault());
        String sadDataTitle = messageSource.getMessage("graph.details.chart.data.sad",
                new Object[]{caaGraph.getSad().size()}, Locale.getDefault());
        String otherDataTitle = messageSource.getMessage("graph.details.chart.data.other",
                new Object[]{otherVertices.size()}, Locale.getDefault());
        PieChart.Data happyVerticesData = new PieChart.Data(happyDataTitle, caaGraph.getHappy().size());
        PieChart.Data sadVerticesData = new PieChart.Data(sadDataTitle, caaGraph.getSad().size());
        PieChart.Data otherVerticesData = new PieChart.Data(otherDataTitle, otherVertices.size());
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        happyVerticesData,
                        sadVerticesData,
                        otherVerticesData);
        this.verticesTypesChart.setData(pieChartData);
        this.verticesTypesChart.setLegendVisible(false);
        happyVerticesData.getNode().setStyle(
                "-fx-pie-color: " + WebUtils.toWeb(preferencesRepository.getHappyVertexColor())
        );
        sadVerticesData.getNode().setStyle(
                "-fx-pie-color: " + WebUtils.toWeb(preferencesRepository.getSadVertexColor())
        );
        otherVerticesData.getNode().setStyle(
                "-fx-pie-color: " + WebUtils.toWeb(preferencesRepository.getVerticesColor())
        );
    }
}
