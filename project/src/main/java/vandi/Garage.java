package vandi;

import java.util.List;
import java.util.ArrayList;

public class Garage {
    private List<PlayerCar> ownedPlayerCars;
    private PlayerCar selectedPlayerCar;

    public Garage() {
        this.ownedPlayerCars = new ArrayList<>();
    }

    public void customize(PlayerCar playerCar) {
        // Customization logic
    }

    public void removeCar(PlayerCar car) {
        if (car == selectedPlayerCar) {
            selectedPlayerCar = null;
        }
        ownedPlayerCars.remove(car);
    }
}