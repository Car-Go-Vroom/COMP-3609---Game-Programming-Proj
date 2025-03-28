package vandi;

import java.util.ArrayList;
import java.util.List;

public class GamePanel {
    private List<CarObject> carObjects;
    private Player player;
    private Bot opponent;
    private Police police;
    private Level current;

    public GamePanel() {
        carObjects = new ArrayList<>();
        player = new Player();
        opponent = new Bot();
        police = new Police();
        current = new Level(1); // This will have to change since we have a main menu and aren't starting in the
                                // game
    }

    /* THIS IS NOT A CONCLUSIVE LIST OF METHODS */
    public void startRace() {
        // Race start logic
    }

    public void endRace() {
        // Race end logic
    }

    public void initiatePolicePursuit() {
        // Police pursuit logic
    }

    public void displayResults() {
        // Results display logic
    }
}