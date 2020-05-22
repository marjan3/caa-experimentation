package com.mtanevski.master.caa.gui.controllers;

import com.mtanevski.master.caa.gui.models.CaaExperimentData;
import com.mtanevski.master.caa.lib.CaaExperimentResults;
import com.mtanevski.master.caa.lib.CaaGraph;
import com.mtanevski.master.caa.lib.CaaTraversalData;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Pagination;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static javafx.collections.FXCollections.observableArrayList;

@Component
public class ExperimentResultsController {

    @FXML
    public Text generationNumberText;
    @FXML
    public Text generationEndText;
    @FXML
    public Text incrementsText;
    @FXML
    public Text traversedEdgesText;
    @FXML
    public Text traversedVerticesText;
    @FXML
    public Text happyToShortestPathFactor;
    @FXML
    public StackedBarChart<String, Number> verticesTraversalsChart;
    @FXML
    public PieChart generationsHappinessChart;
    @FXML
    public Pagination traversalDataHistoryPages;

    @FXML
    public Parent edgesTableView;
    @FXML
    public EdgesTableController edgesTableViewController;

    private final MessageSource messageSource;

    public ExperimentResultsController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setGraphData(CaaExperimentData caaExperimentSaveData) {
        List<CaaTraversalData> history = caaExperimentSaveData.getResults().getHistory();
        CaaGraph caaGraph = history.get(0).getGraph();
        if (history.size() < 1) {
            return;
        }
        setVerticesTraversalsChart(history, caaGraph);
        setShortestPathEfficiency(caaExperimentSaveData.getResults());
        setGenerationsHappinessChart(history);
        setPerGenerationTab(history);
    }

    @FXML
    public void close() {
        Stage stage = (Stage) edgesTableView.getScene().getWindow();
        stage.close();
    }

    private void setShortestPathEfficiency(CaaExperimentResults results) {
        happyToShortestPathFactor.setText(results.getHappyToShortestPathFactor() * 100 + "%");
    }

    private void setPerGenerationTab(List<CaaTraversalData> history) {
        String happyString = messageSource.getMessage("experiment.results.generation.end.happy",
                null, Locale.getDefault());
        String sadString = messageSource.getMessage("experiment.results.generation.end.sad",
                null, Locale.getDefault());
        traversalDataHistoryPages.setPageCount(history.size());
        traversalDataHistoryPages.setMaxPageIndicatorCount(history.size());
        traversalDataHistoryPages.setPageFactory(pageIndex -> {
            CaaTraversalData data = history.get(pageIndex);
            generationNumberText.setText("" + (pageIndex + 1));
            generationEndText.setText(data.isHappy() ? happyString : sadString);
            edgesTableViewController.setTableData(data.getGraph());
            incrementsText.setText("" + data.getIncrements());
            traversedEdgesText.setText("" + data.getTraversedEdges());
            traversedVerticesText.setText("" + data.getTraversedVertices());
            return new VBox();
        });
    }

    private void setVerticesTraversalsChart(List<CaaTraversalData> history, CaaGraph caaGraph) {
        List<String> allVertices = new ArrayList<>(caaGraph.getAllVertices());
        ((CategoryAxis) verticesTraversalsChart.getXAxis()).setCategories(observableArrayList(allVertices));
        List<XYChart.Series<String, Number>> generations = new ArrayList<>();
        String generationNumber = messageSource.getMessage("experiment.results.generation.number",
                null, Locale.getDefault());
        for (int i = 0; i < history.size(); i++) {
            // for each vertex in generation series
            XYChart.Series<String, Number> generation = new XYChart.Series<>();
            generation.setName(generationNumber + (i + 1));
            generations.add(generation);
        }
        for (int i = 0; i < history.size(); i++) {
            // for each vertex in generation series
            CaaTraversalData entry = history.get(i);
            for (String v : allVertices) {
                long howMany = entry.getTraversedVertices().stream().filter(e -> e.equals(v)).count();
                generations.get(i).getData().add(new XYChart.Data<>(v, howMany));
            }
        }

        this.verticesTraversalsChart.getData().addAll(generations);
    }

    private void setGenerationsHappinessChart(List<CaaTraversalData> history) {
        long happyGenerationsCounter = history.stream().filter(CaaTraversalData::isHappy).count();
        long sadGenerationsCounter = history.stream().filter(CaaTraversalData::isSad).count();
        String happyGenerationsCounterName = messageSource.getMessage("experiment.results.happy.generations",
                new Object[]{happyGenerationsCounter}, Locale.getDefault());
        PieChart.Data happyGenerations = new PieChart.Data(happyGenerationsCounterName, happyGenerationsCounter);
        String sadGenerationsCounterName = messageSource.getMessage("experiment.results.sad.generations",
                new Object[]{happyGenerationsCounter}, Locale.getDefault());
        PieChart.Data sadGenerations = new PieChart.Data(sadGenerationsCounterName, sadGenerationsCounter);
        ObservableList<PieChart.Data> pieChartData =
                observableArrayList(
                        happyGenerations,
                        sadGenerations);
        this.generationsHappinessChart.setData(pieChartData);
    }
}
