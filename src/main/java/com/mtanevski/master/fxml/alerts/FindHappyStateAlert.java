package com.mtanevski.master.fxml.alerts;

import com.mtanevski.master.fxml.DefaultValues;
import com.mtanevski.master.lib.caa.impl.agents.CaaAgentFactory;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class FindHappyStateAlert extends Dialog<List<Object>> {

    private final GridPane grid;
    private Spinner<Double> happyIncrement;
    private Spinner<Double> sadDecrement;
    private ComboBox agentComboBox;
    private CheckBox animateBox;

    public FindHappyStateAlert() {
        this.setTitle("Find Happy State");
        this.setHeaderText("Enter details");

        List<Object> results = new ArrayList<>();
        // Set the button types.
        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        // Create the happyIncrement and sadDecrement labels and fields.
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));


        this.getDialogPane().setContent(grid);


        // Convert the result to a username-password-pair when the login button is clicked.
        this.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                if (happyIncrement != null) {
                    results.add(happyIncrement.getValue());
                }
                if (sadDecrement != null) {
                    results.add(sadDecrement.getValue());
                }
                if (agentComboBox != null) {
                    results.add(CaaAgentFactory.CaaAgentType.valueOf(agentComboBox.getSelectionModel().getSelectedItem().toString()));
                }
                if (animateBox != null) {
                    results.add(animateBox.isSelected());
                }
                return results;
            }
            return null;
        });
    }


    public FindHappyStateAlert withIncrements() {
        happyIncrement = createSpinner(DefaultValues.HAPPY_INCREMENTER, DefaultValues.HAPPY_INCREMENTER_MIN_VALUE, DefaultValues.HAPPY_INCREMENTER_MAX_VALUE);
        sadDecrement = createSpinner(DefaultValues.SAD_INCREMENTER, DefaultValues.SAD_INCREMENTER_MIN_VALUE, DefaultValues.SAD_INCREMENTER_MAX_VALUE);

        grid.add(new Label("\"Happy\" increment:"), 0, 0);
        grid.add(happyIncrement, 1, 0);
        grid.add(new Label("\"Sad\" decrement:"), 0, 1);
        grid.add(sadDecrement, 1, 1);

        // Request focus on the username field by default.
        Platform.runLater(happyIncrement::requestFocus);

        return this;
    }

    public FindHappyStateAlert withAgentSelection() {
        agentComboBox = this.createComboBox(CaaAgentFactory.getAvailableAgentTypesAsStrings(), DefaultValues.AGENT_TYPE_VALUE.toString());

        grid.add(new Label("Agent:"), 0, 2);
        grid.add(agentComboBox, 1, 2);

        return this;
    }

    public FindHappyStateAlert withAnimateSelection() {
        animateBox = this.createCheckBox(DefaultValues.SHOULD_ANIMATE_CAA);

        grid.add(new Label("Should animate:"), 0, 3);
        grid.add(animateBox, 1, 3);

        return this;
    }

    private CheckBox createCheckBox(boolean shouldAnimateCaa) {
        CheckBox checkBox = new CheckBox();
        checkBox.setSelected(shouldAnimateCaa);
        return checkBox;
    }

    private ComboBox createComboBox(List<String> values, String selectedValue) {
        ComboBox comboBox =
                new ComboBox(FXCollections
                        .observableArrayList(values));
        comboBox.getSelectionModel().select(selectedValue);
        return comboBox;
    }

    private static Spinner<Double> createSpinner(Double value, Double min, Double max) {
        Spinner<Double> spinner = new Spinner<>();
        spinner.setEditable(true);
        SpinnerValueFactory.DoubleSpinnerValueFactory factory = new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max);
        factory.setAmountToStepBy(0.1);
        spinner.setValueFactory(factory);
        spinner.getValueFactory().setValue(value);
        return spinner;
    }

}
