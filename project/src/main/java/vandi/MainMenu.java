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
import javafx.scene.control.Button;

import java.awt.image.BufferedImage;

public class MainMenu {
    private Stage stage;
    private Scene menuScene;

    // private final double DEFAULT_WIDTH = 800; //Here incase it is needed to be used
    // private final double DEFAULT_HEIGHT = 600; //Here incase it is needed to be used
    private double screenWidth;
    private double screenHeight;

    private Image bgImage;

    public MainMenu(Stage stage) {
        this.stage = stage;
        javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        this.screenWidth = screenBounds.getWidth();
        this.screenHeight = screenBounds.getHeight();
        loadResources();
        createMenuScene();
    }

    private void loadResources() {
        try {
            BufferedImage bufferedImage = ImageManager.loadBufferedImage("/images/background.jpg");
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
            gc.setFill(javafx.scene.paint.Color.LIGHTGRAY);
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

        menuScene = new Scene(root, screenWidth, screenHeight);
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setPrefHeight(200);
        button.setPrefWidth(50);

        button.setStyle(text);//* Add your CSS styles here */

        // TODO: Hover effect for the button
        button.setOnMouseEntered(e -> button.setStyle(text)); // Change style on hover

        // TODO: Add mouse exit effect to reset the button style
        button.setOnMouseExited(e -> button.setStyle(text)); // Reset style on exit
        
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
        // TODO: Implement start functionality here
        // For example, you can switch to the game scene or another menu
    }
    private void handleGarageButtonClick() {
        System.out.println("Garage button clicked.");
        // TODO: Implement garage functionality here
    }
    private void handleStoreButtonClick() {
        System.out.println("Store button clicked.");
        // TODO: Implement store functionality here
    }
    

}
