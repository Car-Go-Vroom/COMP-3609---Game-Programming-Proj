package vandi;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Part {
    private String name;
    private int price;
    private int performanceBoost;
    private double weight;

    public Part(String name, int price, int performanceBoost, double weight) {
        this.name = name;
        this.price = price;
        this.performanceBoost = performanceBoost;
        this.weight = weight;
    }

    public abstract void applyUpgrade(PlayerCar car);
}