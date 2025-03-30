package vandi;

public class Bot {
    private int difficultyLevel;
    private CarObject car;

    public Bot() {
        this.difficultyLevel = 1;
    }

    public Bot(int difficulty) {
        this.difficultyLevel = difficulty;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
}