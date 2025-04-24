package vandi;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerCar extends CarObject {
    private String brand;
    private String model;
    private double topSpeed;
    private double acceleration;
    private double nitroAmount;
    private List<Part> parts;
    private String carSprite; // Assuming Animation is represented as String (THIS NEEDS TO CHANGE)
    private int currentGear;

    public PlayerCar(String brand, String model, double topSpeed,
            double acceleration, double nitroAmount) {
        super();
        this.brand = brand;
        this.model = model;
        this.topSpeed = topSpeed;
        this.acceleration = acceleration;
        this.nitroAmount = nitroAmount;
        this.parts = new ArrayList<>();
        this.currentGear = 1; // default gear
    }

    public double getTopSpeed() {
        return topSpeed;
    }

    public void shiftUp() {
        if (currentGear < 6) { // assuming 6 gears max
            currentGear++;
        }
    }

    public void shiftDown() {
        if (currentGear > 1) {
            currentGear--;
        }
    }

    public void consumeNitro(double amount) {
        nitroAmount -= amount;
        if (nitroAmount < 0) {
            nitroAmount = 0;
        }
    }
}
