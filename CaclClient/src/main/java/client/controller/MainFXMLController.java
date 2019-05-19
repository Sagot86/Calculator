package client.controller;

import client.model.Operation;
import client.service.CalculatorService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.util.List;

public class MainFXMLController implements UpdateHistoryListener {

    private CalculatorService calculatorService = new CalculatorService(this);

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
        calculatorService.clearSB();
        calculatorService.clearUnitForCalc();
        display.setText("0");
    }

    @FXML
    private void clearHist(ActionEvent event) {
        historyWindow.clear();
    }

    @FXML
    private void keyPressedEvent(KeyEvent event) {
        switch (event.getCode()) {
            case PERIOD:
            case DECIMAL:
                pressDot();
                break;
            case DIGIT0:
            case NUMPAD0:
                pressButton("0");
                break;
            case DIGIT1:
            case NUMPAD1:
                pressButton("1");
                break;
            case DIGIT2:
            case NUMPAD2:
                pressButton("2");
                break;
            case DIGIT3:
            case NUMPAD3:
                pressButton("3");
                break;
            case DIGIT4:
            case NUMPAD4:
                pressButton("4");
                break;
            case DIGIT5:
            case NUMPAD5:
                pressButton("5");
                break;
            case DIGIT6:
            case NUMPAD6:
                pressButton("6");
                break;
            case DIGIT7:
            case NUMPAD7:
                pressButton("7");
                break;
            case DIGIT8:
            case NUMPAD8:
                pressButton("8");
                break;
            case DIGIT9:
            case NUMPAD9:
                pressButton("9");
                break;
            case DIVIDE:
                pressOperationButton(Operation.DIVISION);
                break;
            case MULTIPLY:
                pressOperationButton(Operation.MULTIPLICATION);
                break;
            case SUBTRACT:
            case MINUS:
                pressOperationButton(Operation.SUBTRACTION);
                break;
            case ADD:
            case PLUS:
                pressOperationButton(Operation.ADDITION);
                break;
            case EQUALS:
            case ENTER:
                pressOperationButton(Operation.EQUAL);
                break;
        }
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
    }

    private void pressDot() {
        display.setText(calculatorService.processDot("."));
    }

    private void pressButton(String string) {
        display.setText(calculatorService.processValue(string));
    }

    @Override
    public void onHistoryUnitAdded(String unit) {
        historyWindow.appendText(unit + "\n");
    }

}