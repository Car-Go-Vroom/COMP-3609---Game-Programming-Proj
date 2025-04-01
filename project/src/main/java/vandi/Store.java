package vandi;

import java.util.ArrayList;
import java.util.List;

public class Store {
    private List<CarObject> availableCars;
    private List<Part> availableParts;

    public Store() {
        availableCars = new ArrayList<>();
        availableParts = new ArrayList<>();
    }

    public void sellCar(CarObject car, Player player) {
        // Check if car is available
        if (!availableCars.contains(car)) {
            throw new IllegalArgumentException("Car not available in store");
        }

        // Cast to PlayerCar to get price
        PlayerCar playerCar = (PlayerCar) car;
        int carPrice = calculateCarPrice(playerCar);

        // Check if player has enough money
        if (player.getMoney() >= carPrice) {
            // Deduct money from player
            player.deductMoney(carPrice);

            // Add car to player's garage
            player.getGarage().addCar(playerCar);

            // Remove car from store inventory
            availableCars.remove(car);
        } else {
            throw new IllegalStateException("Insufficient Funds");
        }
    }

    private int calculateCarPrice(PlayerCar car) {
        // Base price calculation based on car stats
        return (int) (car.getTopSpeed() * 100 +
                car.getAcceleration() * 1000 +
                car.getNitroAmount() * 50);
    }

    public void sellPart(Part part) {
        // Part selling logic
    }
}
