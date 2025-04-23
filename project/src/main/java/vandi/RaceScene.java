package vandi;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class RaceScene extends Pane {
    private Canvas canvas;
    private GraphicsContext gc;
    private Image backgroundImage;
    private double bgX1;
    private double bgX2;
    private double scrollSpeed = 2; // pixels per frame
    private Player player;

    public RaceScene(double width, double height, Player player) {
        this.player = player;
        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);

        // Load background image using ImageManager
        backgroundImage = javafx.embed.swing.SwingFXUtils.toFXImage(ImageManager.loadBufferedImage("src/main/resources/images/Running and Racing.png"), null);

        // Initialize background positions
        bgX1 = 0;
        bgX2 = backgroundImage.getWidth();

        // Start animation timer for scrolling background
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw();
            }
        };
        timer.start();
    }

    private void update() {
        // Get player's car velocity to control scroll speed
        double velocity = 0;
        if (player != null) {
            CarObject car = player.selectCar();
            if (car != null) {
                velocity = car.getVelocity();
            }
        }

        // Map velocity to scroll speed (adjust scaling factor as needed)
        scrollSpeed = velocity * 0.1;

        // Move backgrounds to the right
        bgX1 += scrollSpeed;
        bgX2 += scrollSpeed;

        // Reset positions when off screen
        if (bgX1 >= canvas.getWidth()) {
            bgX1 = bgX2 - backgroundImage.getWidth();
        }
        if (bgX2 >= canvas.getWidth()) {
            bgX2 = bgX1 - backgroundImage.getWidth();
        }
    }

    private void draw() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(backgroundImage, bgX1, 0, backgroundImage.getWidth(), canvas.getHeight());
        gc.drawImage(backgroundImage, bgX2, 0, backgroundImage.getWidth(), canvas.getHeight());
    }
}
