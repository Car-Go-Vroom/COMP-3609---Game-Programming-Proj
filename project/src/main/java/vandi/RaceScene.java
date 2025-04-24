package vandi;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class RaceScene extends Pane {
    private double width;
    private double height;
    private Player player;
    private RaceGameScreen gameScreen;

    public RaceScene(double width, double height, Player player) {
        this.width = width;
        this.height = height;
        this.player = player;
        
        // Create the game screen
        gameScreen = new RaceGameScreen(width, height, null, player);
        this.getChildren().add(gameScreen);
    }
    
    public void show(Stage stage) {
        gameScreen = new RaceGameScreen(width, height, stage, player);
        this.getChildren().clear();
        this.getChildren().add(gameScreen);
        
        Scene scene = new Scene(this, width, height);
        stage.setScene(scene);
        stage.setTitle("Running & Racing - Race");
        gameScreen.requestFocus();
    }
}