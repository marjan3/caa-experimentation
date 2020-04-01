package com.mtanevski.master.gui.statistics;

import com.mtanevski.master.lib.caa.CaaEdge;
import com.mtanevski.master.lib.caa.CaaExperimenter;
import com.mtanevski.master.lib.caa.CaaGraph;
import com.mtanevski.master.lib.caa.CaaTraversalData;
import com.mtanevski.master.lib.caa.impl.graph.tinker.TinkerCaaEdge;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ExperimentsStatisticsController {

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
    public TableView<CaaEdge> edgesData;
    @FXML
    public Text happyToShortestPathFactor;

    @FXML
    StackedBarChart<String, Number> verticesTraversalsChart;

    @FXML
    PieChart generationsHappinessChart;

    @FXML
    Pagination traversalDataHistoryPages;

    public CaaExperimenter caaExperimenter;

    public void setGraphData(CaaExperimenter caaExperimenter) {
        this.caaExperimenter = caaExperimenter;
        List<CaaTraversalData> history = caaExperimenter.getData().getHistory();
        CaaGraph caaGraph = history.get(0).getGraph();
        if(history.size() < 1){
            return;
        }
        setVerticesTraversalsChart(history, caaGraph);
        setShortestPathEfficiency(caaExperimenter);

        setGenerationsHappinessChart(history);

        setPerGenerationTab(history, caaGraph);
    }

    private void setShortestPathEfficiency(CaaExperimenter caaExperimenter) {
        happyToShortestPathFactor.setText(caaExperimenter.getData().getHappyToShortestPathFactor() * 100 + "%");
    }

    private void setPerGenerationTab(List<CaaTraversalData> history, CaaGraph caaGraph) {
        traversalDataHistoryPages.setPageCount(history.size());
        traversalDataHistoryPages.setMaxPageIndicatorCount(history.size());
        traversalDataHistoryPages.setPageFactory(pageIndex -> {

            CaaTraversalData data = history.get(pageIndex);

            generationNumberText.setText("" + (pageIndex + 1));
            generationEndText.setText(data.isHappy() ? "\"Happy\"" : "\"Sad\"");
            incrementsText.setText("" + data.getIncrements());
            traversedEdgesText.setText("" + data.traversedEdges());
            traversedVerticesText.setText("" + data.traversedVertices());

            TableColumn<CaaEdge, ?> edgesColumn = edgesData.getColumns().get(0);
            TableColumn<CaaEdge, ?> fromColumn = edgesColumn.getColumns().get(0);
            fromColumn.setCellValueFactory(new PropertyValueFactory<>("from"));
            TableColumn<CaaEdge, ?> toColumn = edgesColumn.getColumns().get(1);
            toColumn.setCellValueFactory(new PropertyValueFactory<>("to"));

            TableColumn<CaaEdge, ?> incrementalWeightColumn = edgesData.getColumns().get(1);
            incrementalWeightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
            TableColumn<CaaEdge, ?> traversalWeightColumn = edgesData.getColumns().get(2);
            traversalWeightColumn.setCellValueFactory(new PropertyValueFactory<>("traversalWeight"));

            List<CaaEdge> allEdges = data.getGraph().getAllEdges();
            List<CaaEdge> updatedEdges = new ArrayList<>();
            for (CaaEdge edge:
                    allEdges) {
                String from = tagVertex(caaGraph, edge.getFrom());
                String to = tagVertex(caaGraph, edge.getTo());
                double weight = edge.getWeight();
                double traversalWeight = edge.getTraversalWeight();
                updatedEdges.add(createEdge(from, to, weight, traversalWeight));

            }
            edgesData.getItems().setAll(updatedEdges);
            incrementalWeightColumn.setSortType(TableColumn.SortType.DESCENDING);
            incrementalWeightColumn.setSortable(true);
            edgesData.getSortOrder().add(incrementalWeightColumn);
            return new VBox();
        });
    }

    private CaaEdge createEdge(String from, String to, double weight, double traversalWeight) {
        return new TinkerCaaEdge(from, to, weight, traversalWeight);
    }

    private String tagVertex(CaaGraph caaGraph, String from) {
        if(caaGraph.isVertexHappy(from)){
            from = from + "(happy)";
        } else if(caaGraph.isVertexSad(from)){
            from = from + "(sad)";
        }else if(caaGraph.getStart().equals(from)){
            from = from + "(start)";
        }
        return from;
    }

    private void setVerticesTraversalsChart(List<CaaTraversalData> history, CaaGraph caaGraph) {
        verticesTraversalsChart.setTitle("Vertices Traversals per Generation");

        verticesTraversalsChart.getXAxis().setLabel("Vertices");
        List<String> allVertices = new ArrayList<>(caaGraph.getAllVertices());
        ((CategoryAxis) verticesTraversalsChart.getXAxis()).setCategories(FXCollections.observableArrayList(
                allVertices));
        verticesTraversalsChart.getYAxis().setLabel("Traversals");
        List<XYChart.Series<String, Number>> generations = new ArrayList<>();
        for (int i = 0; i < history.size(); i++) {
            // for each vertex in generation series
            XYChart.Series<String, Number> generation = new XYChart.Series<>();
            generation.setName("Generation #" + (i + 1));
            generations.add(generation);
        }
        for (int i = 0; i < history.size(); i++) {
            // for each vertex in generation series
            CaaTraversalData entry = history.get(i);
            for (String v : allVertices) {
                long howMany = entry.traversedVertices().stream().filter( e -> e.equals(v)).count();
                generations.get(i).getData().add(new XYChart.Data<>(v, howMany));
            }
        }

        this.verticesTraversalsChart.getData().addAll(generations);
    }

    private void setGenerationsHappinessChart(List<CaaTraversalData> history) {
        long happyGenerationsCounter = history.stream().filter(CaaTraversalData::isHappy).count();
        long sadGenerationsCounter = history.stream().filter(CaaTraversalData::isSad).count();
        PieChart.Data happyGenerations = new PieChart.Data(String.format("\"Happy\" (%d)", happyGenerationsCounter), happyGenerationsCounter);
        PieChart.Data sadGenerations = new PieChart.Data(String.format("\"Sad\" (%d)", sadGenerationsCounter), sadGenerationsCounter);
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        happyGenerations,
                        sadGenerations);
        this.generationsHappinessChart.setTitle("\"Happy\" vs \"Sad\" generation");
        this.generationsHappinessChart.setData(pieChartData);
    }

    @FXML
    public void close(){
        Stage stage = (Stage) edgesData.getScene().getWindow();
        stage.close();
    }
}
