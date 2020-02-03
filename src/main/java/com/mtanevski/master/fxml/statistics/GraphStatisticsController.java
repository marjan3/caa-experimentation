package com.mtanevski.master.fxml.statistics;

import com.mtanevski.master.fxml.DefaultValues;
import com.mtanevski.master.fxml.preferences.PreferencesConstants;
import com.mtanevski.master.fxml.preferences.PreferencesController;
import com.mtanevski.master.lib.caa.CaaGraph;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

import static com.mtanevski.master.fxml.visualization.VisOptions.toWeb;

public class GraphStatisticsController {

    private static Preferences p = Preferences.userNodeForPackage(PreferencesController.class);

    public Text startingVertexText;
    public Text happyVerticesText;
    public Text sadVerticesText;
    public TextField verticesText;
    public TextArea edgesText;
    public PieChart verticesTypesChart;


    public void setGraphData(CaaGraph caaGraph) {
        this.startingVertexText.setText(caaGraph.getStart());
        this.happyVerticesText.setText(StringUtils.join(caaGraph.getHappy(), ","));
        this.sadVerticesText.setText(StringUtils.join(caaGraph.getSad(), ","));
        this.verticesText.setText(StringUtils.join(caaGraph.getAllVertices(), ","));
        this.edgesText.setText(StringUtils.join(caaGraph.getAllEdges(), "\n"));

        List<String> otherVertices = caaGraph.getAllVertices().stream()
                .filter(v -> !caaGraph.getSad().contains(v) && !caaGraph.getHappy().contains(v)).collect(Collectors.toList());
        verticesTypesChart.setTitle("Vertices Types Distribution");
        PieChart.Data happyVerticesData = new PieChart.Data(String.format("Happy (%d)", caaGraph.getHappy().size()), caaGraph.getHappy().size());
        PieChart.Data sadVerticesData = new PieChart.Data(String.format("Sad (%d)", caaGraph.getSad().size()), caaGraph.getSad().size());
        PieChart.Data otherVerticesData = new PieChart.Data(String.format("Other (%d)", otherVertices.size()), otherVertices.size());
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        happyVerticesData,
                        sadVerticesData,
                        otherVerticesData);
        this.verticesTypesChart.setData(pieChartData);
        this.verticesTypesChart.setLegendVisible(false);
        happyVerticesData.getNode().setStyle(
                "-fx-pie-color: " + toWeb(p.get(PreferencesConstants.HAPPY_VERTEX_COLOR, DefaultValues.HAPPY_VERTEX_COLOR))
        );
        sadVerticesData.getNode().setStyle(
                "-fx-pie-color: " + toWeb(p.get(PreferencesConstants.SAD_VERTEX_COLOR, DefaultValues.SAD_VERTEX_COLOR))
        );
        otherVerticesData.getNode().setStyle(
                "-fx-pie-color: " + toWeb(p.get(PreferencesConstants.VERTICES_COLOR, DefaultValues.VERTICES_COLOR))
        );
    }
}
