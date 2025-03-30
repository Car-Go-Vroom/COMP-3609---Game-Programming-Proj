package vandi;

public class Level {
    private int levelNumber;
    private String background; // Assuming Image is represented as String
    private double policeProbability;
    private Bot bot;

    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        this.policeProbability = 0.1 * levelNumber;
        this.bot = new Bot(levelNumber);
        this.bot.setDifficultyLevel(levelNumber); // Set difficulty level based on levelNumber
    }

    public void drawBackground(String image) {
        // Background drawing logic
    }

    public Bot getBot() {
        return bot;
    }

    public double getPoliceProbability() {
        return policeProbability;
    }
}