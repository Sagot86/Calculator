package client;

import client.service.InitializeService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;


public class StartClient extends Application {

    public static void main(String[] args) {

        InitializeService service = new InitializeService();

/*        service.onAppStart();

        service.calculate();

        System.out.println("CURRENT IN DB");
        service.printFromDB();

        service.inAppEnd();*/

        StartClient.launch(args);

    }


    @Override
    public void start(Stage stage) throws Exception {

        URL url = new File("CaclClient/src/main/resources/startClient.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root);

        stage.setScene(scene);

        stage.setTitle("Calculator");

        stage.show();

    }
}