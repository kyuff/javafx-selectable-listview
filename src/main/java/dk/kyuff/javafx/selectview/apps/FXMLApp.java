package dk.kyuff.javafx.selectview.apps;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * User: swi
 * Date: 25/01/15
 * Time: 15.24
 */
public class FXMLApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        URL resource = getClass().getResource("view.fxml");
        System.out.println(resource);
        Parent view = FXMLLoader.load(resource);

        primaryStage.setTitle("FXML Selectable Table View");
        primaryStage.setScene(new Scene(view, 500, 500));
        primaryStage.show();

    }
}
