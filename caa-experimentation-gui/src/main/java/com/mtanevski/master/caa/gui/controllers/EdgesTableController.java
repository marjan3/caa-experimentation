package com.mtanevski.master.caa.gui.controllers;

import com.mtanevski.master.caa.lib.CaaEdge;
import com.mtanevski.master.caa.lib.CaaGraph;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class EdgesTableController {

    @FXML
    public TableView<CaaEdge> edgesTable;
    private final MessageSource messageSource;

    public EdgesTableController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setTableData(CaaGraph caaGraph) {
        TableColumn<CaaEdge, ?> edgesColumn = edgesTable.getColumns().get(0);

        TableColumn<CaaEdge, ?> fromColumn = edgesColumn.getColumns().get(0);
        fromColumn.setCellValueFactory(new PropertyValueFactory<>("from"));
        TableColumn<CaaEdge, ?> toColumn = edgesColumn.getColumns().get(1);
        toColumn.setCellValueFactory(new PropertyValueFactory<>("to"));

        TableColumn<CaaEdge, ?> incrementalWeightColumn = edgesTable.getColumns().get(1);
        incrementalWeightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        TableColumn<CaaEdge, ?> traversalWeightColumn = edgesTable.getColumns().get(2);
        traversalWeightColumn.setCellValueFactory(new PropertyValueFactory<>("traversalWeight"));

        List<CaaEdge> allEdges = caaGraph.getAllEdges();
        List<CaaEdge> updatedEdges = new ArrayList<>();
        for (CaaEdge edge : allEdges) {
            String from = tagVertex(caaGraph, edge.getFrom());
            String to = tagVertex(caaGraph, edge.getTo());
            double weight = edge.getWeight();
            double traversalWeight = edge.getTraversalWeight();
            updatedEdges.add(new CaaEdge(from, to, weight, traversalWeight));
        }
        edgesTable.getItems().setAll(updatedEdges);
        incrementalWeightColumn.setSortType(TableColumn.SortType.DESCENDING);
        incrementalWeightColumn.setSortable(true);
        edgesTable.getSortOrder().add(incrementalWeightColumn);
    }

    private String tagVertex(CaaGraph caaGraph, String from) {
        if (caaGraph.isVertexHappy(from)) {
            from = from + messageSource.getMessage("edges.table.happy.tag", null, Locale.getDefault());
        } else if (caaGraph.isVertexSad(from)) {
            from = from + messageSource.getMessage("edges.table.sad.tag", null, Locale.getDefault());
        } else if (caaGraph.getStart().equals(from)) {
            from = from + messageSource.getMessage("edges.table.start.tag", null, Locale.getDefault());
        }
        return from;
    }
}
