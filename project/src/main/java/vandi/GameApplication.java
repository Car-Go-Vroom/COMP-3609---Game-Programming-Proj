package vandi;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GameApplication extends Application {
    @Override
    public void start(Stage stage) {
        MainMenu mainMenu = new MainMenu(stage);
        mainMenu.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}