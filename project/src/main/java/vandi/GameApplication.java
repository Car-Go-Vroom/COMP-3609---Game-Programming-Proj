package vandi;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GameApplication extends Application {
    @Override
    public void start(Stage stage) {
        Label label = new Label("Game is running!");
        Scene scene = new Scene(new StackPane(label), 300, 200);
        stage.setScene(scene);
        stage.setTitle("Game Window");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}