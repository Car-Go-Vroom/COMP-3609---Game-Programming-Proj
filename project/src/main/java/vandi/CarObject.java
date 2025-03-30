package vandi;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class CarObject {
    private double positionX;
    private double positionY;
    private int width;
    private int height;
    private double velocity;
    private double weight;

    public CarObject(double positionX, double positionY, int width,
            int height, double velocity, double weight) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.width = width;
        this.height = height;
        this.velocity = velocity;
        this.weight = weight;
    }

    public CarObject() {
        // TODO Auto-generated constructor stub
        // This may not be necessary
    }
}