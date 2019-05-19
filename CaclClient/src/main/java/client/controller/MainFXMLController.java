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
        pressButton(".");
    }

    @FXML
    private void pressZero(ActionEvent event) {
        pressButton("0");
    }

    @FXML
    private void pressOne(ActionEvent event) {
        pressButton("1");
    }

    @FXML
    private void pressTwo(ActionEvent event) {
        pressButton("2");
    }

    @FXML
    private void pressThree(ActionEvent event) {
        pressButton("3");
    }

    @FXML
    private void pressFour(ActionEvent event) {
        pressButton("4");
    }

    @FXML
    private void pressFive(ActionEvent event) {
        pressButton("5");
    }

    @FXML
    private void pressSix(ActionEvent event) {
        pressButton("6");
    }

    @FXML
    private void pressSeven(ActionEvent event) {
        pressButton("7");
    }

    @FXML
    private void pressEight(ActionEvent event) {
        pressButton("8");
    }

    @FXML
    private void pressNine(ActionEvent event) {
        pressButton("9");
    }

    @FXML
    private void pressDivide(ActionEvent event) {
        pressOperationButton(Operation.DIVISION);
    }

    @FXML
    private void pressMult(ActionEvent event) {
        pressOperationButton(Operation.MULTIPLICATION);
    }

    @FXML
    private void pressSubt(ActionEvent event) {
        pressOperationButton(Operation.SUBTRACTION);
    }

    @FXML
    private void pressAdd(ActionEvent event) {
        pressOperationButton(Operation.ADDITION);
    }

    @FXML
    private void pressEqual(ActionEvent event) {
        pressOperationButton(Operation.EQUAL);
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
        /* Печать истории из базы */
        List<String> toPrint = calculatorService.getHistory();
        for (String string : toPrint) {
            historyWindow.appendText(string + "\n");
        }

        /* Установка начального значения дисплея */
        display.setText("0");
    }

    private void pressOperationButton(Operation operation) {
        String s = calculatorService.getInputValue(operation);
        display.setText(s);
        if (calculatorService.checkFin()) {
            historyWindow.appendText(calculatorService.getStr());
            calculatorService.setShouldPringFalse();
        }
    }

    private void pressButton(String string) {
        display.setText(calculatorService.getInputValue(string));
    }

}