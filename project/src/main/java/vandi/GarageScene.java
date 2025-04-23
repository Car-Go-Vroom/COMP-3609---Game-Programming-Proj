package vandi;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class GarageScene {
    private Stage stage;
    private Scene garageScene;
    private MainMenu mainMenu;
    
    private double screenWidth;
    private double screenHeight;
    
    // For car navigation
    private List<PlayerCar> playerCars;
    private int currentCarIndex = 0;
    
    // UI Elements
    private ImageView carImageView;
    private Label brandLabel;
    private Label modelLabel;
    private Label speedLabel;
    private Label nitroLabel;
    
    public GarageScene(Stage stage, MainMenu mainMenu) {
        this.stage = stage;
        this.mainMenu = mainMenu;
        
        // Get screen dimensions
        javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        this.screenWidth = screenBounds.getWidth();
        this.screenHeight = screenBounds.getHeight();
        
        // Create dummy car data (replace with actual data from player)
        createDummyCars();
        
        // Create the scene
        createGarageScene();
    }
    
    private void createDummyCars() {
        // This would be replaced with actual player car data
        playerCars = new ArrayList<>();
        
        PlayerCar car1 = new PlayerCar("Ferrari", "Enzo", 220, 5.2, 150);
        car1.setWeight(1200);
        
        PlayerCar car2 = new PlayerCar("Nissan", "Fairlady Z", 180, 4.5, 100);
        car2.setWeight(1400);
        
        PlayerCar car3 = new PlayerCar("Shelby", "GT500 1967", 170, 4.0, 75);
        car3.setWeight(1600);
        
        playerCars.add(car1);
        playerCars.add(car2);
        playerCars.add(car3);
    }
    
    private void createGarageScene() {
        StackPane root = new StackPane();
        
        // garage background image
        try {
            BufferedImage bgBuffer = ImageManager.loadBufferedImage("src/main/resources/images/garage_background.png");
            if (bgBuffer != null) {
                Image bgImage = SwingFXUtils.toFXImage(bgBuffer, null);
                ImageView backgroundView = new ImageView(bgImage);
                backgroundView.setFitWidth(screenWidth);
                backgroundView.setFitHeight(screenHeight);
                backgroundView.setPreserveRatio(false);
                root.getChildren().add(backgroundView);
            } else {
                // Fallback if image not found
                root.setStyle("-fx-background-color: #1E1E28;");
            }
        } catch (Exception e) {
            System.out.println("Error loading garage background: " + e.getMessage());
            root.setStyle("-fx-background-color: #1E1E28;");
        }
        
        // Content layout
        BorderPane contentPane = new BorderPane();
        
        // Top bar
        HBox topBar = createTopBar();
        contentPane.setTop(topBar);
        
        // Car display area
        StackPane carDisplayArea = createCarDisplayArea();
        contentPane.setCenter(carDisplayArea);
        
        // Customize button box
        HBox bottomBar = createBottomBar();
        contentPane.setBottom(bottomBar);
        
        // Left arrow
        VBox leftNav = createLeftNavigation();
        contentPane.setLeft(leftNav);
        
        // Right arrow
        VBox rightNav = createRightNavigation();
        contentPane.setRight(rightNav);
        
        // Car details
        VBox carDetailsPanel = createCarDetailsPanel();
        carDetailsPanel.setTranslateX(40);
        carDetailsPanel.setTranslateY(100);
        
        // Add all elements to the root
        root.getChildren().addAll(contentPane, carDetailsPanel);
        
        // Create the scene
        garageScene = new Scene(root, screenWidth, screenHeight);
        
        // Initial update of car details
        updateCarDisplay();
    }
    
    private HBox createTopBar() {
        HBox topBar = new HBox(50);
        topBar.setPadding(new Insets(20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        
        // Title
        Label titleLabel = new Label("GARAGE");
        titleLabel.setFont(Font.font("Impact", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.WHITE);
        
        // Back button
        Button backButton = createStyledButton("Back to Menu");
        backButton.setOnAction(e -> returnToMainMenu());
        
        // Add some spacing to push money to the right
        HBox spacer = new HBox();
        spacer.setMinWidth(150);
        spacer.setPrefWidth(Integer.MAX_VALUE);
        
        // Player money (placeholder)
        Label moneyLabel = new Label("$50,000");  // Replace with actual player money
        moneyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        moneyLabel.setTextFill(Color.YELLOW);
        
        topBar.getChildren().addAll(backButton, titleLabel, spacer, moneyLabel);
        
        // Semi-transparent background
        topBar.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-border-color: #444; -fx-border-width: 0 0 2 0;");
        
        return topBar;
    }
    
    private StackPane createCarDisplayArea() {
        StackPane carDisplay = new StackPane();
        carDisplay.setPadding(new Insets(20));
        
        // Car image view
        carImageView = new ImageView();
        carImageView.setFitWidth(900);
        carImageView.setPreserveRatio(true);
        
        // Car info
        VBox carInfo = new VBox(10);
        carInfo.setAlignment(Pos.CENTER);
        carInfo.setPadding(new Insets(20));
        carInfo.setMaxWidth(800);
        carInfo.setTranslateY(150); // Position below the car
        carInfo.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-border-color: #444; -fx-border-width: 1px; -fx-border-radius: 5px;");
        
        Font pixelFont = null;
        try {
            pixelFont = Font.loadFont(new FileInputStream(new File("src/main/resources/fonts/PressStart2P-Regular.ttf")), 12); // https://fonts.google.com/specimen/Press+Start+2P
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Font file not found. Using default font.");
        }

        Label carNameLabel = new Label();
        carNameLabel.setFont(pixelFont);
        carNameLabel.setTextFill(Color.WHITE);
        
        HBox statsBox = new HBox(0);
        statsBox.setAlignment(Pos.CENTER);
        
        Label speedStatsLabel = new Label();
        speedStatsLabel.setFont(pixelFont);
        speedStatsLabel.setTextFill(Color.WHITE);
        
        Label accelLabel = new Label();
        accelLabel.setFont(pixelFont);
        accelLabel.setTextFill(Color.WHITE);
        
        Label nitroStatsLabel = new Label();
        nitroStatsLabel.setFont(pixelFont);
        nitroStatsLabel.setTextFill(Color.WHITE);
        
        Label weightLabel = new Label();
        weightLabel.setFont(pixelFont);
        weightLabel.setTextFill(Color.WHITE);
        
        statsBox.getChildren().addAll(speedStatsLabel, accelLabel, nitroStatsLabel, weightLabel);
        carInfo.getChildren().addAll(carNameLabel, statsBox);
        
        carDisplay.getChildren().addAll(carImageView, carInfo);
        
        return carDisplay;
    }
    
    private VBox createCarDetailsPanel() {
        // Create a VBox for the car details that will appear in the top left
        VBox detailsPanel = new VBox(12); // Spacing between lines
        detailsPanel.setPadding(new Insets(20));
        
        // Use a pixelated/retro font style
        Font pixelFont = null;

        // Font loader for the pixelized font for main menu text
        try {
            pixelFont = Font.loadFont(new FileInputStream(new File("src/main/resources/fonts/PressStart2P-Regular.ttf")), 30); // https://fonts.google.com/specimen/Press+Start+2P
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Font file not found. Using default font.");
        }
        
        // Car brand
        brandLabel = new Label("Car Brand:");
        brandLabel.setFont(pixelFont);
        brandLabel.setTextFill(Color.WHITE);
        
        // Car model
        modelLabel = new Label("Car Model:");
        modelLabel.setFont(pixelFont);
        modelLabel.setTextFill(Color.WHITE);
        
        // Car speed
        speedLabel = new Label("Top Speed:");
        speedLabel.setFont(pixelFont);
        speedLabel.setTextFill(Color.WHITE);
        
        // Car nitro
        nitroLabel = new Label("Nitro Capacity:");
        nitroLabel.setFont(pixelFont);
        nitroLabel.setTextFill(Color.WHITE);
        
        detailsPanel.getChildren().addAll(brandLabel, modelLabel, speedLabel, nitroLabel);
        
        return detailsPanel;
    }
    
    private HBox createBottomBar() {
        HBox bottomBar = new HBox();
        bottomBar.setPadding(new Insets(20));
        bottomBar.setAlignment(Pos.CENTER);
        
        // Customize button (prominent in the middle)
        Button customizeButton = createStyledButton("Customize Car");
        // customizeButton.setPrefWidth(300);
        customizeButton.setPrefHeight(50);

        // Apply style similar to home screen buttons (text only, no background)
        Font pixelFont = null;
        try {
            pixelFont = Font.loadFont(new FileInputStream(new File("src/main/resources/fonts/PressStart2P-Regular.ttf")), 30);
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Font file not found. Using default font.");
        }
        customizeButton.setFont(pixelFont);

        customizeButton.setStyle(
            "-fx-font-family: '" + (pixelFont != null ? pixelFont.getFamily() : "System") + "', monospace;" +
            "-fx-text-fill: white;" +
            "-fx-background-color: transparent;" +
            "-fx-border-width: 0;" +
            "-fx-font-size: 24px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 3, 0, 3, 3);" +
            "-fx-padding: 10 20;" +
            "-fx-cursor: hand;" +
            "-fx-text-transform: uppercase;" +
            "-fx-letter-spacing: 2px"
        );

        // Add action to open the customization scene (when implemented)
        customizeButton.setOnAction(e -> {
            System.out.println("Customize car clicked. (Not implemented yet)");
        });

        bottomBar.getChildren().add(customizeButton);
        
        // Semi-transparent background
        bottomBar.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-border-color: #444; -fx-border-width: 2 0 0 0;");
        
        return bottomBar;
    }
    
    private VBox createLeftNavigation() {
        VBox leftNav = new VBox();
        leftNav.setPadding(new Insets(0, 10, 0, 20));
        leftNav.setAlignment(Pos.CENTER);
        
        // Left arrow button
        Button leftArrow = new Button("←");
        leftArrow.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        leftArrow.setPrefWidth(100);
        leftArrow.setPrefHeight(80);
        
        leftArrow.setStyle(
            "-fx-background-color: rgba(0, 0, 0, 0.5); " +
            "-fx-text-fill: white; " +
            "-fx-border-color: #444; " +
            "-fx-border-width: 1px; " +
            "-fx-cursor: hand; " +
            "-fx-border-radius: 5px;"
        );
        
        // Add action to navigate to previous car
        leftArrow.setOnAction(e -> previousCar());
        
        leftNav.getChildren().add(leftArrow);
        
        return leftNav;
    }
    
    private VBox createRightNavigation() {
        VBox rightNav = new VBox();
        rightNav.setPadding(new Insets(0, 20, 0, 10));
        rightNav.setAlignment(Pos.CENTER);
        
        // Right arrow button
        Button rightArrow = new Button("→");
        rightArrow.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        rightArrow.setPrefWidth(100);
        rightArrow.setPrefHeight(80);
        
        rightArrow.setStyle(
            "-fx-background-color: rgba(0, 0, 0, 0.5); " +
            "-fx-text-fill: white; " +
            "-fx-border-color: #444; " +
            "-fx-border-width: 1px; " +
            "-fx-cursor: hand; " +
            "-fx-border-radius: 5px;"
        );
        
        // Add action to navigate to next car
        rightArrow.setOnAction(e -> nextCar());
        
        rightNav.getChildren().add(rightArrow);
        
        return rightNav;
    }
    
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setPrefHeight(40);
        // button.setPrefWidth(300);
        Font pixelFont = null;

        // Font loader for the pixelized font for text
        try {
            pixelFont = Font.loadFont(new FileInputStream(new File("src/main/resources/fonts/PressStart2P-Regular.ttf")), 30); // https://fonts.google.com/specimen/Press+Start+2P
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Font file not found. Using default font.");
        }
        
        button.setStyle(
            "-fx-font-family: '"+ pixelFont.getFamily() +"';" +
            "-fx-font-size: 12px;" +
            "-fx-background-color: #444444;" +
            "-fx-text-fill: white;" +
            "-fx-border-color: white;" +
            "-fx-border-width: 2px;" +
            "-fx-cursor: hand;"
        );
        
        // Hover effect
        button.setOnMouseEntered(e -> 
            button.setStyle(
                "-fx-background-color: #666666;" +
                "-fx-text-fill: white;" +
                "-fx-border-color: #aaaaaa;" +
                "-fx-border-width: 2px;" +
                "-fx-cursor: hand;"
            )
        );
        
        // Reset style on exit
        button.setOnMouseExited(e -> 
            button.setStyle(
                "-fx-background-color: #444444;" +
                "-fx-text-fill: white;" +
                "-fx-border-color: white;" +
                "-fx-border-width: 2px;" +
                "-fx-cursor: hand;"
            )
        );
        
        return button;
    }
    
    private void returnToMainMenu() {
        mainMenu.show();
    }
    
    // Navigation methods
    private void nextCar() {
        if (playerCars.isEmpty()) return;
        
        currentCarIndex = (currentCarIndex + 1) % playerCars.size();
        updateCarDisplay();
    }
    
    private void previousCar() {
        if (playerCars.isEmpty()) return;
        
        currentCarIndex = (currentCarIndex - 1 + playerCars.size()) % playerCars.size();
        updateCarDisplay();
    }
    
    // Update UI with current car data
    private void updateCarDisplay() {
        if (playerCars.isEmpty()) return;
        
        PlayerCar currentCar = playerCars.get(currentCarIndex);
        
        // Update car image
        try {
            String imagePath = "src/main/resources/images/cars/nissan_fairlady_240z.png";
            BufferedImage carBuffer = ImageManager.loadBufferedImage(imagePath);
            if (carBuffer != null) {
                Image carImage = SwingFXUtils.toFXImage(carBuffer, null);
                carImageView.setImage(carImage);
            }
        } catch (Exception e) {
            System.out.println("Error loading car image: " + e.getMessage());
        }
        
        // Update car details panel (top left)
        brandLabel.setText("Car Brand: " + currentCar.getBrand());
        modelLabel.setText("Car Model: " + currentCar.getModel());
        speedLabel.setText("Top Speed: " + currentCar.getTopSpeed() + " mph");
        nitroLabel.setText("Nitro Capacity: " + (int)currentCar.getNitroAmount());
        
        // Update car info below the car
        ((Label)((VBox)carImageView.getParent().getChildrenUnmodifiable().get(1)).getChildren().get(0))
            .setText(currentCar.getBrand() + " " + currentCar.getModel());
            
        HBox statsBox = (HBox)((VBox)carImageView.getParent().getChildrenUnmodifiable().get(1)).getChildren().get(1);
        ((Label)statsBox.getChildren().get(0)).setText("Speed: " + currentCar.getTopSpeed() + " mph");
        ((Label)statsBox.getChildren().get(1)).setText("Acceleration: " + currentCar.getAcceleration() + " m/s²");
        ((Label)statsBox.getChildren().get(2)).setText("Nitro: " + (int)currentCar.getNitroAmount());
        ((Label)statsBox.getChildren().get(3)).setText("Weight: " + (int)currentCar.getWeight() + " kg");
    }
    
    public void show() {
        stage.setScene(garageScene);
        stage.setTitle("Running & Racing - Garage");
    }
}