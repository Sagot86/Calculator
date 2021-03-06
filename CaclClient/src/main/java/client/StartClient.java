package client;

import client.service.DataTransferService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class StartClient extends Application {

    /** ужасно, переделать позже */
    private DataTransferService service = new DataTransferService();

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

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/startClient.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Calculator");
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        service.onAppStop();
        super.stop();
    }
}