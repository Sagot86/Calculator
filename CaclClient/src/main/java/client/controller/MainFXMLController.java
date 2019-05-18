package client.controller;

import client.model.Operation;
import client.service.CalculatorService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.List;

public class MainFXMLController {

    private CalculatorService calculatorService = new CalculatorService();

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
        display.setText(calculatorService.getInputValue("."));
    }

    @FXML
    private void pressZero(ActionEvent event) {
        display.setText(calculatorService.getInputValue("0"));
    }

    @FXML
    private void pressOne(ActionEvent event) {
        display.setText(calculatorService.getInputValue("1"));
    }

    @FXML
    private void pressTwo(ActionEvent event) {
        display.setText(calculatorService.getInputValue("2"));
    }

    @FXML
    private void pressThree(ActionEvent event) {
        display.setText(calculatorService.getInputValue("3"));
    }

    @FXML
    private void pressFour(ActionEvent event) {
        display.setText(calculatorService.getInputValue("4"));
    }

    @FXML
    private void pressFive(ActionEvent event) {
        display.setText(calculatorService.getInputValue("5"));
    }

    @FXML
    private void pressSix(ActionEvent event) {
        display.setText(calculatorService.getInputValue("6"));
    }

    @FXML
    private void pressSeven(ActionEvent event) {
        display.setText(calculatorService.getInputValue("7"));
    }

    @FXML
    private void pressEight(ActionEvent event) {
        display.setText(calculatorService.getInputValue("8"));
    }

    @FXML
    private void pressNine(ActionEvent event) {
        display.setText(calculatorService.getInputValue("9"));
    }

    @FXML
    private void pressDivide(ActionEvent event) {
        String s = calculatorService.getInputValue(Operation.DIVISION);
        display.setText(s);
        if (calculatorService.checkFin()) {
            historyWindow.appendText(calculatorService.getStr());
            calculatorService.setShouldPringFalse();
        }
    }

    @FXML
    private void pressMult(ActionEvent event) {
        String s = calculatorService.getInputValue(Operation.MULTIPLICATION);
        display.setText(s);
        if (calculatorService.checkFin()) {
            historyWindow.appendText(calculatorService.getStr());
            calculatorService.setShouldPringFalse();
        }
    }

    @FXML
    private void pressSubt(ActionEvent event) {
        String s = calculatorService.getInputValue(Operation.SUBTRACTION);
        display.setText(s);
        if (calculatorService.checkFin()) {
            historyWindow.appendText(calculatorService.getStr());
            calculatorService.setShouldPringFalse();
        }

    }

    @FXML
    private void pressAdd(ActionEvent event) {
        String s = calculatorService.getInputValue(Operation.ADDITION);
        display.setText(s);
        if (calculatorService.checkFin()) {
            historyWindow.appendText(calculatorService.getStr());
            calculatorService.setShouldPringFalse();
        }
    }

    @FXML
    private void pressEqual(ActionEvent event) {
        String s = calculatorService.getInputValue(Operation.EQUAL);
        display.setText(s);
        if (calculatorService.checkFin()) {
            historyWindow.appendText(calculatorService.getStr());
            calculatorService.setShouldPringFalse();
        }
    }

    @FXML
    private void pressCancel(ActionEvent event) {
        calculatorService.clearSB ();
        calculatorService.clearUnitForCalc();
        display.setText("0");
    }

    @FXML
    private void clearHist(ActionEvent event) {
        historyWindow.clear();
    }

    @FXML
    private void initialize() {
        List<String> toPrint = calculatorService.getHistory();
        for (String string : toPrint) {
            historyWindow.appendText(string + "\n");
        }
        display.setText("0");
    }

}