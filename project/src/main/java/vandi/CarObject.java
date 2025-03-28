package vandi;

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

    public double getPositionX() {
        return positionX;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}