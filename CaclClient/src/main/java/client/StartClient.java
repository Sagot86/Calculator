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

    /** ужасно, переделать позже */
    private InitializeService service = new InitializeService();

    public static void main(String[] args) {
        StartClient.launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        service.onAppStart();

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

    @Override
    public void stop() throws Exception {
        service.onAppStop();
        super.stop();
    }
}