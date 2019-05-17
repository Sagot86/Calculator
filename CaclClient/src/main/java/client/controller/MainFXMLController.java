package client.controller;

import client.model.Operation;
import client.service.InitializeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.List;

public class MainFXMLController {

    private InitializeService initializeService = new InitializeService();

    @FXML
    private TextArea historyWindow;

    @FXML
    private TextField display;

    @FXML
    private Button clearHistButton;

    @FXML
    private Button equalButton;

    @FXML
    private Button divideButton;

    @FXML
    private Button multiplyButton;

    @FXML
    private Button subtrButton;

    @FXML
    private Button addButton;

    @FXML
    private Button sevenButton;

    @FXML
    private Button eightButton;

    @FXML
    private Button nineButton;

    @FXML
    private Button fourButton;

    @FXML
    private Button fiveButton;

    @FXML
    private Button sixButton;

    @FXML
    private Button oneButton;

    @FXML
    private Button twoButton;

    @FXML
    private Button threeButton;

    @FXML
    private Button zeroButton;

    @FXML
    private Button dotButton;

    @FXML
    private Button cancelButton;

    @FXML
    private void pressDot(ActionEvent event) {
        display.setText(initializeService.getInputValue("."));
    }

    @FXML
    private void pressZero(ActionEvent event) {
        display.setText(initializeService.getInputValue("0"));
    }

    @FXML
    private void pressOne(ActionEvent event) {
        display.setText(initializeService.getInputValue("1"));
    }

    @FXML
    private void pressTwo(ActionEvent event) {
        display.setText(initializeService.getInputValue("2"));
    }

    @FXML
    private void pressThree(ActionEvent event) {
        display.setText(initializeService.getInputValue("3"));
    }

    @FXML
    private void pressFour(ActionEvent event) {
        display.setText(initializeService.getInputValue("4"));
    }

    @FXML
    private void pressFive(ActionEvent event) {
        display.setText(initializeService.getInputValue("5"));
    }

    @FXML
    private void pressSix(ActionEvent event) {
        display.setText(initializeService.getInputValue("6"));
    }

    @FXML
    private void pressSeven(ActionEvent event) {
        display.setText(initializeService.getInputValue("7"));
    }

    @FXML
    private void pressEight(ActionEvent event) {
        display.setText(initializeService.getInputValue("8"));
    }

    @FXML
    private void pressNine(ActionEvent event) {
        display.setText(initializeService.getInputValue("9"));
    }

    @FXML
    private void pressDivide(ActionEvent event) {
        display.setText(initializeService.getInputValue(Operation.DIVISION));
    }

    @FXML
    private void pressMult(ActionEvent event) {
        display.setText(initializeService.getInputValue(Operation.MULTIPLICATION));
    }

    @FXML
    private void pressSubt(ActionEvent event) {
        display.setText(initializeService.getInputValue(Operation.SUBTRACTION));
    }

    @FXML
    private void pressAdd(ActionEvent event) {
        display.setText(initializeService.getInputValue(Operation.ADDITION));
    }

    @FXML
    private void pressEqual(ActionEvent event) {
        display.setText(initializeService.getInputValue(Operation.EQUAL));
    }

    @FXML
    private void pressCancel(ActionEvent event) {
        initializeService.clearSB ();
        initializeService.clearUnitForCalc();
        display.setText("0");
    }

    @FXML
    private void clearHist(ActionEvent event) {
        historyWindow.clear();
    }


    @FXML
    private void initialize() {
        List<String> toPrint = initializeService.getHistory();
        for (String string : toPrint) {
            historyWindow.appendText(string + "\n");
        }
        display.setText("0");
    }

}