package vandi;

import java.util.ArrayList;
import java.util.List;


/* This is how to select cars
PlayerCar car = new PlayerCar("Toyota", "Supra", 200, 5.0, 100);
player.selectCarFromGarage(car);
CarObject selectedCar = player.selectCar();

 * This is how to get the store to sell cars
  try {
    store.sellCar(selectedCar, player);
} catch (IllegalStateException e) {
    System.out.println(e.getMessage()); // "Insufficient Funds"
} catch (IllegalArgumentException e) {
    System.out.println(e.getMessage()); // "Car not available in store"
} */

public class GamePanel {
    private List<CarObject> carObjects;
    /*
     * Location 0 in the list will be the playerCar
     * Location 1 in the list will be the opponent
     * Location 2 in the list will be the police Car
     */
    private Player player;
    private Bot opponent;
    private Police police;
    private Level current;
    private RaceStats rs;

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
        double random = Math.random();
        if (random < current.getPoliceProbability()) {
            police.setActive(true);

            PlayerCar playerCar = (PlayerCar) carObjects.get(0);
            // I suspect this is only going to register that specific position so I may have
            // to throw this in a thread to continuously get the position but later for
            // that.
            double targetX = playerCar.getPositionX();
            double targetY = playerCar.getPositionY();

            police.chase(targetX, targetY, player, playerCar);
        }
    }

    public void displayResults() {
        rs.toString(); // I have no idea what this gonna output so good luck lombok.
    }
}