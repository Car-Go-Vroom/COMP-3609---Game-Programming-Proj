package vandi;

public class Level {
    private int levelNumber;
    private String background; // Assuming Image is represented as String
    private double policeProbability;

    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        this.policeProbability = 0.1 * levelNumber;
    }

    public void drawBackground(String image) {
        // Background drawing logic
    }
}
