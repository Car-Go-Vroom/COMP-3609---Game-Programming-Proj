package vandi;

import java.util.List;
import java.util.ArrayList;

public class Garage {
    private List<PlayerCar> ownedPlayerCars;
    private PlayerCar selectedPlayerCar;

    public Garage() {
        this.ownedPlayerCars = new ArrayList<>();
    }

    public void customize(PlayerCar playerCar, Part part) {
        if (!ownedPlayerCars.contains(playerCar)) {
            return;
        }

        playerCar.getParts().add(part);

        part.applyUpgrade(playerCar);

        playerCar.setWeight(playerCar.getWeight() + part.getWeight());
    }

    public void customize(PlayerCar playerCar, List<Part> parts) {
        if (!ownedPlayerCars.contains(playerCar)) {
            return;
        }

        for (Part part : parts) {
            playerCar.getParts().add(part);

            part.applyUpgrade(playerCar);

            playerCar.setWeight(playerCar.getWeight() + part.getWeight());
        }
    }

    public void removeCar(PlayerCar car) {
        if (car == selectedPlayerCar) {
            selectedPlayerCar = null;
        }
        ownedPlayerCars.remove(car);
    }

    public PlayerCar getSelectedPlayerCar() {
        return selectedPlayerCar;
    }

    public void setSelectedPlayerCar(PlayerCar car) {
        if (ownedPlayerCars.contains(car)) {
            selectedPlayerCar = car;
        }
    }

    public List<PlayerCar> getOwnedPlayerCars() {
        return ownedPlayerCars;
    }

    public void addCar(PlayerCar car) {
        ownedPlayerCars.add(car);
    }
}