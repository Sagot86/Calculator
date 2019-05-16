package client.controller;

import client.service.InitializeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;


import java.util.List;

public class MainFXMLController {

    private InitializeService initializeService = new InitializeService();

    @FXML
    private TextArea historyWindow;

    @FXML
    private Button loadHistButton;

    @FXML
    private Button clearHistButton;

    @FXML
    private void loadHist(ActionEvent event) {
        List<String> toPrint = initializeService.getHistory();
        for (String string: toPrint) {
            historyWindow.appendText(string + "\n");
        }
    }

    @FXML
    private void clearHist(ActionEvent event) {
        historyWindow.clear();
    }

}