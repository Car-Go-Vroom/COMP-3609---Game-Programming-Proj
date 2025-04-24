package vandi;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

public class RaceGameScreen extends Pane {
    // Game state
    private boolean raceStarted = false;
    private boolean raceFinished = false;
    private boolean countingDown = false;
    private int countdown = 3;
    private long lastCountdownTime = 0;
    
    // Screen dimensions
    private final double screenWidth;
    private final double screenHeight;
    
    // Game elements
    private Canvas backgroundCanvas;
    private GraphicsContext bgContext;
    private AnchorPane uiPane;
    
    // Cars
    private ImageView playerCarView;
    private ImageView opponentCarView;
    private double playerCarX;
    private double playerCarY;
    private double opponentCarX;
    private double opponentCarY;
    
    // Background scrolling
    private Image backgroundImage;
    private double bgX1 = 0;
    private double bgX2 = 0;
    private double scrollSpeed = 0;
    private final double MAX_SCROLL_SPEED = 15;
    
    // Race indicators
    private Rectangle nitroBar;
    private Rectangle nitroBarBackground;
    private Label speedLabel;
    private Label rpmLabel;
    private Label gearLabel;
    private Label countdownLabel;
    private ImageView tachometerView;
    private ImageView tachNeedleView;
    private ImageView trafficLightView;
    
    // Game parameters
    private double playerSpeed = 0;
    private double opponentSpeed = 0;
    private double playerAcceleration = 0.5;
    private double opponentAcceleration = 0.45;
    private double playerTopSpeed = 120;
    private double opponentTopSpeed = 110;
    private int currentGear = 1;
    private int opponentGear = 1;
    private final int MAX_GEAR = 5;
    private double rpm = 0;
    private double opponentRpm = 0;
    private final double MAX_RPM = 8000;
    private final double SHIFT_RPM = 7000;
    private double nitroAmount = 100;
    private boolean nitroActive = false;
    private double distanceTraveled = 0;
    private final double RACE_DISTANCE = 402; // 1/4 mile in meters
    
    // Race statistics
    private double raceStartTime = 0;
    private double reactionTime = 0;
    private double quarterMileTime = 0;
    
    // Player car controls
    private boolean accelerating = false;
    private boolean braking = false;
    private boolean shiftingUp = false;
    private boolean shiftingDown = false;
    private boolean usingNitro = false;
    
    private AnimationTimer gameLoop;
    private Stage stage;
    private Player player;
    private Bot opponent;
    
    public RaceGameScreen(double width, double height, Stage stage, Player player) {
        this.screenWidth = width;
        this.screenHeight = height;
        this.stage = stage;
        this.player = player;
        this.opponent = new Bot(1); // Start with difficulty level 1
        
        // Initialize game parameters from player's car if available
        if (player != null && player.selectCar() != null) {
            PlayerCar selectedCar = (PlayerCar) player.selectCar();
            this.playerTopSpeed = selectedCar.getTopSpeed();
            this.playerAcceleration = selectedCar.getAcceleration();
            // Adjust opponent based on player's car strength and difficulty
            this.opponentTopSpeed = playerTopSpeed * 0.9;
            this.opponentAcceleration = playerAcceleration * 0.85;
        }
        
        // Initialize UI elements
        initializeGameScreen();
        
        // Set up game loop
        setupGameLoop();
        
        // Set up controls
        setupControls();
    }
    
    private void initializeGameScreen() {
        // Background canvas for scrolling
        backgroundCanvas = new Canvas(screenWidth, screenHeight);
        bgContext = backgroundCanvas.getGraphicsContext2D();
        
        // UI pane for overlaid elements
        uiPane = new AnchorPane();
        
        // Add all elements to the main pane
        this.getChildren().addAll(backgroundCanvas, uiPane);
        
        // Load images
        loadImages();
        
        // Initialize cars
        initializeCars();
        
        // Initialize UI elements
        initializeUI();
    }
    
    private void loadImages() {
        try {
            // Load background image (you'll need to create/provide this)
            BufferedImage bgBuffer = ImageManager.loadBufferedImage("src/main/resources/images/race_background.png");
            if (bgBuffer == null) {
                // Create a fallback background if image not found
                bgBuffer = createFallbackBackground();
            }
            backgroundImage = SwingFXUtils.toFXImage(bgBuffer, null);
            
            // Initialize second background image position
            bgX2 = backgroundImage.getWidth();
            
            // Load tachometer
            BufferedImage tachoBuffer = ImageManager.loadBufferedImage("src/main/resources/images/tachometer.png");
            if (tachoBuffer != null) {
                tachometerView = new ImageView(SwingFXUtils.toFXImage(tachoBuffer, null));
                tachometerView.setFitWidth(200);
                tachometerView.setFitHeight(200);
                tachometerView.setPreserveRatio(true);
            }
            
            // Load tachometer needle
            BufferedImage needleBuffer = ImageManager.loadBufferedImage("src/main/resources/images/tach_needle.png");
            if (needleBuffer != null) {
                tachNeedleView = new ImageView(SwingFXUtils.toFXImage(needleBuffer, null));
                tachNeedleView.setFitWidth(100);
                tachNeedleView.setFitHeight(100);
                tachNeedleView.setPreserveRatio(true);
            }
            
            // Load traffic light
            BufferedImage lightBuffer = ImageManager.loadBufferedImage("src/main/resources/images/traffic_light.png");
            if (lightBuffer != null) {
                trafficLightView = new ImageView(SwingFXUtils.toFXImage(lightBuffer, null));
                trafficLightView.setFitWidth(80);
                trafficLightView.setFitHeight(150);
                trafficLightView.setPreserveRatio(true);
            }
        } catch (Exception e) {
            System.out.println("Error loading images: " + e.getMessage());
        }
    }
    
    private BufferedImage createFallbackBackground() {
        // Create a simple background with a road and sky if image is not available
        BufferedImage bg = new BufferedImage(1200, 600, BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics2D g2d = bg.createGraphics();
        
        // Sky
        g2d.setColor(new java.awt.Color(135, 206, 235));
        g2d.fillRect(0, 0, 1200, 400);
        
        // Draw some clouds
        g2d.setColor(java.awt.Color.WHITE);
        g2d.fillOval(100, 100, 100, 50);
        g2d.fillOval(300, 150, 120, 60);
        g2d.fillOval(600, 80, 150, 70);
        g2d.fillOval(900, 120, 100, 50);
        
        // Road
        g2d.setColor(java.awt.Color.DARK_GRAY);
        g2d.fillRect(0, 400, 1200, 200);
        
        // Road lines
        g2d.setColor(java.awt.Color.WHITE);
        for (int i = 0; i < 12; i++) {
            g2d.fillRect(i * 100, 500, 50, 10);
        }
        
        g2d.dispose();
        return bg;
    }
    
    private void initializeCars() {
        try {
            // Try to load player's selected car if available
            BufferedImage playerCarBuffer = null;
            if (player != null && player.selectCar() != null) {
                PlayerCar selectedCar = (PlayerCar) player.selectCar();
                String carImagePath = "src/main/resources/images/cars/" + selectedCar.getBrand().toLowerCase() + "_" + 
                                    selectedCar.getModel().toLowerCase().replace(" ", "_") + ".png";
                playerCarBuffer = ImageManager.loadBufferedImage(carImagePath);
            }
            
            // Fallback to default car if selected car image not found
            if (playerCarBuffer == null) {
                playerCarBuffer = ImageManager.loadBufferedImage("src/main/resources/images/cars/nissan_fairlady_240z.png");
            }
            
            if (playerCarBuffer != null) {
                playerCarView = new ImageView(SwingFXUtils.toFXImage(playerCarBuffer, null));
                playerCarView.setFitWidth(200);
                playerCarView.setFitHeight(100);
                playerCarView.setPreserveRatio(true);
                
                // Position the player car
                playerCarX = 150;
                playerCarY = screenHeight - 200;
                playerCarView.setLayoutX(playerCarX);
                playerCarView.setLayoutY(playerCarY);
                
                uiPane.getChildren().add(playerCarView);
            }
            
            // Load opponent car image
            BufferedImage opponentCarBuffer = ImageManager.loadBufferedImage("src/main/resources/images/cars/ferrari_enzo.png");
            if (opponentCarBuffer == null) {
                // Use player car if opponent car not found
                opponentCarBuffer = playerCarBuffer;
            }
            
            if (opponentCarBuffer != null) {
                opponentCarView = new ImageView(SwingFXUtils.toFXImage(opponentCarBuffer, null));
                opponentCarView.setFitWidth(200);
                opponentCarView.setFitHeight(100);
                opponentCarView.setPreserveRatio(true);
                
                // Position the opponent car
                opponentCarX = 150;
                opponentCarY = screenHeight - 300;
                opponentCarView.setLayoutX(opponentCarX);
                opponentCarView.setLayoutY(opponentCarY);
                
                uiPane.getChildren().add(opponentCarView);
            }
        } catch (Exception e) {
            System.out.println("Error loading car images: " + e.getMessage());
        }
    }
    
    private void initializeUI() {
        // Load pixel font
        Font pixelFont = null;
        try {
            pixelFont = Font.loadFont(new FileInputStream(new File("src/main/resources/fonts/PressStart2P-Regular.ttf")), 16);
        } catch (Exception e) {
            System.out.println("Font not found, using default: " + e.getMessage());
            pixelFont = Font.font("System", FontWeight.BOLD, 16);
        }
        
        // Speed indicator
        speedLabel = new Label("0 MPH");
        speedLabel.setFont(pixelFont);
        speedLabel.setTextFill(Color.WHITE);
        speedLabel.setLayoutX(20);
        speedLabel.setLayoutY(20);
        speedLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-padding: 5px;");
        
        // RPM indicator
        rpmLabel = new Label("0 RPM");
        rpmLabel.setFont(pixelFont);
        rpmLabel.setTextFill(Color.WHITE);
        rpmLabel.setLayoutX(20);
        rpmLabel.setLayoutY(60);
        rpmLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-padding: 5px;");
        
        // Gear indicator
        gearLabel = new Label("GEAR: 1");
        gearLabel.setFont(pixelFont);
        gearLabel.setTextFill(Color.WHITE);
        gearLabel.setLayoutX(20);
        gearLabel.setLayoutY(100);
        gearLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-padding: 5px;");
        
        // Nitro bar background
        nitroBarBackground = new Rectangle(screenWidth - 220, 20, 200, 25);
        nitroBarBackground.setFill(Color.rgb(50, 50, 50, 0.7));
        nitroBarBackground.setStroke(Color.WHITE);
        
        // Nitro bar
        nitroBar = new Rectangle(screenWidth - 220, 20, 200, 25);
        nitroBar.setFill(Color.LIME);
        
        // Nitro label
        Label nitroLabel = new Label("NITRO");
        nitroLabel.setFont(pixelFont);
        nitroLabel.setTextFill(Color.WHITE);
        nitroLabel.setLayoutX(screenWidth - 270);
        nitroLabel.setLayoutY(23);
        
        // Countdown label
        countdownLabel = new Label("");
        countdownLabel.setFont(Font.font(pixelFont.getFamily(), FontWeight.BOLD, 72));
        countdownLabel.setTextFill(Color.YELLOW);
        countdownLabel.setLayoutX(screenWidth / 2 - 40);
        countdownLabel.setLayoutY(screenHeight / 3);
        
        // Position tachometer (bottom right)
        if (tachometerView != null) {
            tachometerView.setLayoutX(screenWidth - 220);
            tachometerView.setLayoutY(screenHeight - 220);
            uiPane.getChildren().add(tachometerView);
            
            // Position tachometer needle on top of tachometer
            if (tachNeedleView != null) {
                tachNeedleView.setLayoutX(screenWidth - 170);
                tachNeedleView.setLayoutY(screenHeight - 170);
                // Set pivot point for rotation
                tachNeedleView.setRotate(0);
                uiPane.getChildren().add(tachNeedleView);
            }
        }
        
        // Position traffic light (center top)
        if (trafficLightView != null) {
            trafficLightView.setLayoutX(screenWidth / 2 - 40);
            trafficLightView.setLayoutY(50);
            uiPane.getChildren().add(trafficLightView);
        }
        
        // Add all UI elements to the pane
        uiPane.getChildren().addAll(
            speedLabel, 
            rpmLabel, 
            gearLabel,
            nitroBarBackground, 
            nitroBar, 
            nitroLabel,
            countdownLabel
        );
        
        // Distance indicator
        Label distanceLabel = new Label("DISTANCE: 0m");
        distanceLabel.setFont(pixelFont);
        distanceLabel.setTextFill(Color.WHITE);
        distanceLabel.setLayoutX(screenWidth / 2 - 100);
        distanceLabel.setLayoutY(20);
        distanceLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-padding: 5px;");
        uiPane.getChildren().add(distanceLabel);
        
        // Start button
        Label startInstructions = new Label("Press SPACE to start race");
        startInstructions.setFont(pixelFont);
        startInstructions.setTextFill(Color.WHITE);
        startInstructions.setLayoutX(screenWidth / 2 - 150);
        startInstructions.setLayoutY(screenHeight - 50);
        uiPane.getChildren().add(startInstructions);
    }
    
    private void setupGameLoop() {
        gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;
            
            @Override
            public void handle(long now) {
                // Calculate delta time in seconds
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }
                double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;
                
                update(deltaTime);
                render();
            }
        };
        gameLoop.start();
    }
    
    private void update(double deltaTime) {
        // Handle countdown
        if (countingDown) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastCountdownTime > 1000) {
                countdown--;
                lastCountdownTime = currentTime;
                
                if (countdown <= 0) {
                    countingDown = false;
                    raceStarted = true;
                    countdownLabel.setText("GO!");
                    raceStartTime = System.currentTimeMillis() / 1000.0;
                    
                    // Hide countdown after a short delay
                    new Thread(() -> {
                        try {
                            Thread.sleep(1000);
                            javafx.application.Platform.runLater(() -> {
                                countdownLabel.setText("");
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                } else {
                    countdownLabel.setText(String.valueOf(countdown));
                }
            }
            return;
        }
        
        // Don't update game state if race hasn't started or is finished
        if (!raceStarted || raceFinished) {
            return;
        }
        
        // Track reaction time
        if (raceStartTime > 0 && reactionTime == 0 && playerSpeed > 0) {
            reactionTime = (System.currentTimeMillis() / 1000.0) - raceStartTime;
        }
        
        // Update player car
        updatePlayerCar(deltaTime);
        
        // Update opponent car
        updateOpponentCar(deltaTime);
        
        // Update relative positions based on speed difference
        updateRelativePositions(deltaTime);
        
        // Update nitro
        updateNitro(deltaTime);
        
        // Update distance traveled
        // Convert MPH to meters per second (1 mph = 0.44704 m/s)
        double speedInMetersPerSecond = playerSpeed * 0.44704;
        distanceTraveled += speedInMetersPerSecond * deltaTime;
        
        // Update distance label
        for (javafx.scene.Node node : uiPane.getChildren()) {
            if (node instanceof Label && ((Label) node).getText().startsWith("DISTANCE:")) {
                ((Label) node).setText(String.format("DISTANCE: %.0fm", distanceTraveled));
                break;
            }
        }
        
        // Check if race is finished
        if (distanceTraveled >= RACE_DISTANCE) {
            quarterMileTime = (System.currentTimeMillis() / 1000.0) - raceStartTime;
            finishRace();
        }
    }
    
    private void updatePlayerCar(double deltaTime) {
        // Handle acceleration
        if (accelerating) {
            // Accelerate based on current gear
            double gearEfficiency = 1.0 - (0.15 * currentGear);
            rpm += (3000 * gearEfficiency) * deltaTime;
            
            // Limit RPM to MAX_RPM
            if (rpm > MAX_RPM) {
                rpm = MAX_RPM;
            }
            
            // Calculate speed based on RPM and gear
            playerSpeed = (rpm / 1000) * currentGear * 2;
            
            // Apply nitro boost if active
            if (nitroActive && nitroAmount > 0) {
                playerSpeed *= 1.5;
            }
            
            // Limit to top speed
            if (playerSpeed > playerTopSpeed) {
                playerSpeed = playerTopSpeed;
            }
        } else {
            // Deceleration when not accelerating
            rpm -= 1000 * deltaTime;
            playerSpeed -= 10 * deltaTime;
        }
        
        // Handle braking
        if (braking) {
            playerSpeed -= 40 * deltaTime;
            rpm -= 2000 * deltaTime;
        }
        
        // Ensure speed and RPM don't go negative
        if (playerSpeed < 0) playerSpeed = 0;
        if (rpm < 0) rpm = 0;
        
        // Handle gear shifting
        if (shiftingUp && rpm > SHIFT_RPM) {
            shiftUp();
        }
        
        if (shiftingDown) {
            shiftDown();
        }
        
        // Update scroll speed based on player speed
        scrollSpeed = playerSpeed / 8;
        if (scrollSpeed > MAX_SCROLL_SPEED) {
            scrollSpeed = MAX_SCROLL_SPEED;
        }
        
        // Update UI elements
        speedLabel.setText(String.format("%.0f MPH", playerSpeed));
        rpmLabel.setText(String.format("%.0f RPM", rpm));
        
        // Rotate tachometer needle
        if (tachNeedleView != null) {
            double angle = -135 + (rpm / MAX_RPM) * 270; // -135 to 135 degree range
            tachNeedleView.setRotate(angle);
        }
        
        // Add exhaust particles when accelerating hard
        if (accelerating && rpm > 6000 && Math.random() > 0.7) {
            addExhaustParticles(playerCarX + 20, playerCarY + 80);
        }
    }
    
    private void updateOpponentCar(double deltaTime) {
        // Simple AI for opponent - adjust based on difficulty
        double opponentThinkTime = Math.random();
        
        // Calculate opponent RPM
        opponentRpm = (opponentSpeed / (2 * opponentGear)) * 1000;
        
        // Opponent will shift up when RPM is high enough
        if (opponentRpm > SHIFT_RPM * 0.9 && opponentThinkTime > 0.7) {
            // Shift up logic for opponent
            int nextGear = Math.min(opponentGear + 1, MAX_GEAR);
            if (nextGear > opponentGear) {
                // Opponent shifts up
                opponentSpeed = opponentSpeed * opponentGear / nextGear;
                opponentGear = nextGear;
                opponentRpm /= 2;
            }
        }
        
        // Opponent accelerates
        opponentRpm += (3000 * (1.0 - 0.15 * opponentGear)) * deltaTime;
        if (opponentRpm > MAX_RPM) opponentRpm = MAX_RPM;
        
        // Calculate speed based on RPM and gear
        opponentSpeed = (opponentRpm / 1000) * opponentGear * 2;
        
        // Limit to opponent top speed
        if (opponentSpeed > opponentTopSpeed) {
            opponentSpeed = opponentTopSpeed;
        }
        
        // Add exhaust particles for opponent too
        if (opponentRpm > 6000 && Math.random() > 0.8) {
            addExhaustParticles(opponentCarX + 20, opponentCarY + 80);
        }
    }
    
    private void updateRelativePositions(double deltaTime) {
        // Calculate relative speed difference
        double speedDifference = playerSpeed - opponentSpeed;
        
        // Move opponent car based on speed difference
        // If player is faster, opponent moves left; if slower, opponent moves right
        opponentCarX -= speedDifference * deltaTime * 0.5;
        
        // Limit opponent car position to stay somewhat on screen
        if (opponentCarX < -150) {
            opponentCarX = -150;
        } else if (opponentCarX > screenWidth) {
            opponentCarX = screenWidth;
        }
        
        // Update opponent car view position
        opponentCarView.setLayoutX(opponentCarX);
    }
    
    private void updateNitro(double deltaTime) {
        // Decrease nitro when active
        if (nitroActive && nitroAmount > 0) {
            nitroAmount -= 20 * deltaTime;
            if (nitroAmount < 0) {
                nitroAmount = 0;
                nitroActive = false;
            }
            
            // Change nitro bar color based on amount
            if (nitroAmount < 30) {
                nitroBar.setFill(Color.RED);
            } else {
                nitroBar.setFill(Color.LIME);
            }
            
            // Add nitro particles
            if (Math.random() > 0.5) {
                addNitroParticles(playerCarX + 30, playerCarY + 70);
            }
        } else if (!nitroActive && nitroAmount < 100) {
            // Recharge nitro when not active
            nitroAmount += 5 * deltaTime;
            if (nitroAmount > 100) {
                nitroAmount = 100;
            }
            nitroBar.setFill(Color.LIME);
        }
        
        // Update nitro bar
        nitroBar.setWidth((nitroAmount / 100) * 200);
    }
    
    // Triple-buffered background for smoother scrolling
    private Image[] backgroundBuffer;
    private double[] bgPositions;
    private int numBackgroundCopies = 3; // Triple buffer
    
    private void initializeBackgroundBuffer() {
        if (backgroundImage == null) return;
        
        // Initialize the background buffer if not already done
        if (backgroundBuffer == null) {
            backgroundBuffer = new Image[numBackgroundCopies];
            bgPositions = new double[numBackgroundCopies];
            
            // Create copies of the background image
            for (int i = 0; i < numBackgroundCopies; i++) {
                backgroundBuffer[i] = backgroundImage;
                bgPositions[i] = i * backgroundImage.getWidth();
            }
        }
    }
    
    private void render() {
        // Clear the canvas
        bgContext.clearRect(0, 0, screenWidth, screenHeight);
        
        // Initialize background buffer if needed
        if (backgroundBuffer == null && backgroundImage != null) {
            initializeBackgroundBuffer();
        }
        
        // Draw the scrolling background with triple buffering
        if (backgroundBuffer != null) {
            // Move all backgrounds based on scroll speed
            for (int i = 0; i < numBackgroundCopies; i++) {
                bgPositions[i] -= scrollSpeed;
                
                // Draw the background
                bgContext.drawImage(backgroundBuffer[i], bgPositions[i], 0, 
                                   backgroundBuffer[i].getWidth(), screenHeight);
                
                // If this background has moved completely off screen to the left
                if (bgPositions[i] + backgroundBuffer[i].getWidth() < 0) {
                    // Find the rightmost background
                    double rightmostPos = -Double.MAX_VALUE;
                    int rightmostIndex = 0;
                    for (int j = 0; j < numBackgroundCopies; j++) {
                        if (bgPositions[j] > rightmostPos) {
                            rightmostPos = bgPositions[j];
                            rightmostIndex = j;
                        }
                    }
                    
                    // Position this off-screen background to the right of the rightmost one
                    bgPositions[i] = bgPositions[rightmostIndex] + backgroundBuffer[rightmostIndex].getWidth();
                }
            }
        }
        // Fallback to old method if buffer initialization failed
        else if (backgroundImage != null) {
            bgContext.drawImage(backgroundImage, bgX1, 0, backgroundImage.getWidth(), screenHeight);
            bgContext.drawImage(backgroundImage, bgX2, 0, backgroundImage.getWidth(), screenHeight);
            
            // Move backgrounds to the left for scrolling effect
            bgX1 -= scrollSpeed;
            bgX2 -= scrollSpeed;
            
            // Reset positions when off screen
            if (bgX1 + backgroundImage.getWidth() <= 0) {
                bgX1 = bgX2 + backgroundImage.getWidth();
            }
            if (bgX2 + backgroundImage.getWidth() <= 0) {
                bgX2 = bgX1 + backgroundImage.getWidth();
            }
        }
    }
    
    private void setupControls() {
        this.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case SPACE:
                    if (!raceStarted && !countingDown) {
                        startCountdown();
                    } else if (raceFinished) {
                        resetRace();
                    }
                    break;
                case UP:
                    accelerating = true;
                    break;
                case DOWN:
                    braking = true;
                    break;
                case RIGHT:
                    shiftingUp = true;
                    break;
                case LEFT:
                    shiftingDown = true;
                    break;
                case N:
                    if (nitroAmount > 0) {
                        nitroActive = true;
                    }
                    break;
                case ESCAPE:
                    returnToMainMenu();
                    break;
            }
        });
        
        this.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case UP:
                    accelerating = false;
                    break;
                case DOWN:
                    braking = false;
                    break;
                case RIGHT:
                    shiftingUp = false;
                    break;
                case LEFT:
                    shiftingDown = false;
                    break;
                case N:
                    nitroActive = false;
                    break;
            }
        });
        
        // Request focus so key events work
        this.setFocusTraversable(true);
        this.requestFocus();
    }
    
    private void startCountdown() {
        countingDown = true;
        countdown = 3;
        lastCountdownTime = System.currentTimeMillis();
        countdownLabel.setText(String.valueOf(countdown));
    }
    
    private void shiftUp() {
        if (currentGear < MAX_GEAR) {
            currentGear++;
            // When shifting up, RPM drops
            rpm = Math.max(rpm / 2, 1000);
            
            // Update gear display
            gearLabel.setText("GEAR: " + currentGear);
            
            // Play shift sound
            try {
                SoundManager.getInstance().playClip("shift", false);
            } catch (Exception e) {
                // Sound manager not initialized or clip not found
            }
        }
        shiftingUp = false;
    }
    
    private void shiftDown() {
        if (currentGear > 1) {
            currentGear--;
            // When shifting down, RPM increases
            rpm = Math.min(rpm * 1.5, MAX_RPM);
            
            // Update gear display
            gearLabel.setText("GEAR: " + currentGear);
            
            // Play downshift sound
            try {
                SoundManager.getInstance().playClip("downshift", false);
            } catch (Exception e) {
                // Sound manager not initialized or clip not found
            }
        }
        shiftingDown = false;
    }
    
    private void finishRace() {
        if (raceFinished) return; // Prevent multiple calls
        
        raceFinished = true;
        
        // Determine winner
        boolean playerWon = playerSpeed > opponentSpeed;
        
        // Show result
        countdownLabel.setText(playerWon ? "YOU WIN!" : "YOU LOSE!");
        countdownLabel.setLayoutX(screenWidth / 2 - 150);
        
        // Show race statistics
        showRaceStats(playerWon);
        
        // Update player's race stats
        if (player != null) {
            RaceStats playerStats = player.getRaceStats();
            if (playerStats != null) {
                if (playerWon) {
                    // Award money for winning
                    int moneyEarned = 1000;
                    // Add money to player
                    player.addMoney(moneyEarned);
                }
            }
        }
        
        // Show restart instructions
        Label restartLabel = new Label("Press SPACE to restart");
        restartLabel.setFont(countdownLabel.getFont().font(20));
        restartLabel.setTextFill(Color.WHITE);
        restartLabel.setLayoutX(screenWidth / 2 - 150);
        restartLabel.setLayoutY(screenHeight / 2);
        uiPane.getChildren().add(restartLabel);
    }
    
    private void showRaceStats(boolean playerWon) {
        VBox statsBox = new VBox(10);
        statsBox.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-padding: 20px;");
        statsBox.setLayoutX(screenWidth/2 - 150);
        statsBox.setLayoutY(screenHeight/2 - 100);
        
        // Create stats labels
        Label titleLabel = new Label("RACE RESULTS");
        titleLabel.setTextFill(Color.YELLOW);
        
        Label quarterMileLabel = new Label("Quarter Mile Time: " + String.format("%.2f", quarterMileTime) + "s");
        quarterMileLabel.setTextFill(Color.WHITE);
        
        Label reactionTimeLabel = new Label("Reaction Time: " + String.format("%.3f", reactionTime) + "s");
        reactionTimeLabel.setTextFill(Color.WHITE);
        
        Label topSpeedLabel = new Label("Top Speed: " + String.format("%.0f", playerSpeed) + " MPH");
        topSpeedLabel.setTextFill(Color.WHITE);
        
        Label resultLabel = new Label(playerWon ? "Result: WIN" : "Result: LOSS");
        resultLabel.setTextFill(playerWon ? Color.GREEN : Color.RED);
        
        // Add reward info if player won
        Label rewardLabel = null;
        if (playerWon) {
            rewardLabel = new Label("Reward: $1000");
            rewardLabel.setTextFill(Color.GREEN);
        }
        
        // Add all labels to the stats box
        statsBox.getChildren().addAll(titleLabel, quarterMileLabel, reactionTimeLabel, topSpeedLabel, resultLabel);
        if (rewardLabel != null) {
            statsBox.getChildren().add(rewardLabel);
        }
        
        uiPane.getChildren().add(statsBox);
    }
    
    private void resetRace() {
        // Reset race state variables
        raceStarted = false;
        raceFinished = false;
        countingDown = false;
        playerSpeed = 0;
        opponentSpeed = 0;
        rpm = 0;
        opponentRpm = 0;
        currentGear = 1;
        opponentGear = 1;
        nitroAmount = 100;
        nitroActive = false;
        distanceTraveled = 0;
        raceStartTime = 0;
        reactionTime = 0;
        quarterMileTime = 0;
        
        // Reset UI elements
        speedLabel.setText("0 MPH");
        rpmLabel.setText("0 RPM");
        gearLabel.setText("GEAR: 1");
        nitroBar.setWidth(200);
        countdownLabel.setText("");
        
        // Reset opponent car position
        opponentCarX = 150;
        opponentCarView.setLayoutX(opponentCarX);
        
        // Reset tachometer needle
        if (tachNeedleView != null) {
            tachNeedleView.setRotate(-135);
        }
        
        // Reset distance label
        for (javafx.scene.Node node : uiPane.getChildren()) {
            if (node instanceof Label && ((Label) node).getText().startsWith("DISTANCE:")) {
                ((Label) node).setText("DISTANCE: 0m");
                break;
            }
        }
        
        // Remove extra UI elements
        uiPane.getChildren().removeIf(node -> 
            (node instanceof Label && ((Label) node).getText().equals("Press SPACE to restart")) ||
            (node instanceof VBox && node.getStyle().contains("rgba(0,0,0,0.8)"))
        );
        
        // Reset controls
        setupControls();
    }
    
    private void returnToMainMenu() {
        if (stage != null) {
            MainMenu mainMenu = new MainMenu(stage);
            mainMenu.show();
        }
    }
    
    private void addExhaustParticles(double x, double y) {
        // Create small particles that fade out
        Rectangle particle = new Rectangle(4, 4, Color.rgb(150, 150, 150, 0.7));
        particle.setLayoutX(x);
        particle.setLayoutY(y + (Math.random() * 5) - 2.5);
        uiPane.getChildren().add(particle);
        
        // Animate the particle
        FadeTransition fade = new FadeTransition(Duration.millis(600), particle);
        fade.setFromValue(0.7);
        fade.setToValue(0);
        
        // Also move the particle slightly
        javafx.animation.TranslateTransition move = new javafx.animation.TranslateTransition(Duration.millis(600), particle);
        move.setByX(-20 - (Math.random() * 10));
        move.setByY((Math.random() * 6) - 3);
        
        // Start animations
        fade.play();
        move.play();
        
        // Remove particle when animation is done
        fade.setOnFinished(e -> uiPane.getChildren().remove(particle));
    }
    
    private void addNitroParticles(double x, double y) {
        // Create blue flame particles for nitro
        Rectangle particle = new Rectangle(6, 6);
        
        // Alternate between blue and light blue
        if (Math.random() > 0.5) {
            particle.setFill(Color.rgb(0, 150, 255, 0.8));
        } else {
            particle.setFill(Color.rgb(100, 200, 255, 0.8));
        }
        
        particle.setLayoutX(x - 10);
        particle.setLayoutY(y + (Math.random() * 8) - 4);
        uiPane.getChildren().add(particle);
        
        // Animate the particle
        FadeTransition fade = new FadeTransition(Duration.millis(400), particle);
        fade.setFromValue(0.8);
        fade.setToValue(0);
        
        // Also move the particle
        javafx.animation.TranslateTransition move = new javafx.animation.TranslateTransition(Duration.millis(400), particle);
        move.setByX(-30 - (Math.random() * 20));
        move.setByY((Math.random() * 10) - 5);
        
        // Start animations
        fade.play();
        move.play();
        
        // Remove particle when animation is done
        fade.setOnFinished(e -> uiPane.getChildren().remove(particle));
    }
    
    public void show(Stage stage) {
        Scene scene = new Scene(this, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.setTitle("Running & Racing - Race");
        this.requestFocus();
    }
}