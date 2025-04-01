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

    public PlayerCar(String brand, String model, double topSpeed,
            double acceleration, double nitroAmount) {
        super();
        this.brand = brand;
        this.model = model;
        this.topSpeed = topSpeed;
        this.acceleration = acceleration;
        this.nitroAmount = nitroAmount;
        this.parts = new ArrayList<>();
    }

    public double getTopSpeed() {
        return topSpeed;
    }
}