package vandi;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.control.Button;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

import vandi.RaceScene;
import vandi.GarageScene;

public class MainMenu {
    private Stage stage;
    private Scene menuScene;
    // private Shop shopScene = null; // Initialize shopScene to null
    private Player player;

    private double screenWidth;
    private double screenHeight;

    private Image bgImage;

    public MainMenu(Stage stage) {
        this.stage = stage;
        javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        this.screenWidth = screenBounds.getWidth();
        this.screenHeight = screenBounds.getHeight();
        player = new Player();
        loadResources();
        createMenuScene();
    }

    private void loadResources() {
        try {
            BufferedImage bufferedImage = ImageManager.loadBufferedImage("src/main/resources/images/Running and Racing.png");
            Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
            bgImage = fxImage;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createMenuScene() {
        StackPane root = new StackPane();
        Canvas canvas = new Canvas(screenWidth, screenHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        if (bgImage != null) {
            gc.drawImage(bgImage, 0, 0, screenWidth, screenHeight);
        } else {
            gc.setFill(javafx.scene.paint.Color.RED);
            gc.fillRect(0, 0, screenWidth, screenHeight);
            System.out.println("Background image not loaded.");
        }
        root.getChildren().add(canvas);
        // Container for the buttons
        VBox buttonBox = new VBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        // Creating the standard buttons
        Button startButton = createStyledButton("Start");
        Button garageButton = createStyledButton("Garage");
        Button storeButton =  createStyledButton("Store");

        startButton.setOnAction(e -> handleStartButtonClick());
        garageButton.setOnAction(e -> handleGarageButtonClick());
        storeButton.setOnAction(e -> handleStoreButtonClick());

        // Adding buttons to the VBox
        buttonBox.getChildren().addAll(startButton, garageButton, storeButton);
        buttonBox.setTranslateY(80); // Adjust the Y position of the button box
        root.getChildren().add(buttonBox);

        menuScene = new Scene(root, screenWidth, screenHeight);
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setPrefHeight(50);
        button.setPrefWidth(200);
        Font pixelFont = null;

        // Font loader for the pixelized font for main menu text
        try {
            pixelFont = Font.loadFont(new FileInputStream(new File("src/main/resources/fonts/PressStart2P-Regular.ttf")), 30); // https://fonts.google.com/specimen/Press+Start+2P
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Font file not found. Using default font.");
        }

        button.setStyle(
            "-fx-font-family: '"+ pixelFont.getFamily() +"', monospace;" +
            "-fx-text-fill: white;"+
            "-fx-background-color: transparent;" +
            "-fx-border-width: 0;" +
            "-fx-font-size: 24px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 3, 0, 3, 3);" +
            "-fx-padding: 10 20;" +
            "-fx-cursor: hand;" +
            "-fx-text-transform: uppercase;"+
            "-fx-letter-spacing: 2px" 
        );

        // TODO: Hover effect for the button
        // button.setOnMouseEntered(e -> button.setStyle(text)); // Change style on hover

        // TODO: Add mouse exit effect to reset the button style
        // button.setOnMouseExited(e -> button.setStyle(text)); // Reset style on exit

        return button;
    }

    public void show() {
        stage.setScene(menuScene);
        stage.setTitle("Running & Racing");
        stage.setResizable(true);
        stage.show();
    }

    private void handleStartButtonClick() {
        System.out.println("Start button clicked.");
        // Switch to race scene with player instance
        RaceScene raceScene = new RaceScene(screenWidth, screenHeight, player);
        Scene scene = new Scene(raceScene, screenWidth, screenHeight);
        stage.setScene(scene);
    }
    private void handleGarageButtonClick() {
        System.out.println("Garage button clicked.");
        // TODO: Implement garage functionality here
        GarageScene garageScene = new GarageScene(stage, this);
        garageScene.show(); // Show the garage scene
    }
    private void handleStoreButtonClick() {
        // System.out.println("Store button clicked.");
        // if(shopScene == null){
        //     shopScene = new Shop(stage); // Assuming you have a Shop class that creates the shop scene
        // }
        // shopScene.show(); // Show the shop scene
    }
    
}
