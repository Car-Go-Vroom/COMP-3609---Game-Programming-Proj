package vandi;

public class EnginePart extends Part {
    public EnginePart(String name, int price, int performanceBoost, double weight) {
        super(name, price, performanceBoost, weight);
    }

    @Override
    public void applyUpgrade(PlayerCar car) {
        car.setTopSpeed(car.getTopSpeed() + getPerformanceBoost());
    }
}