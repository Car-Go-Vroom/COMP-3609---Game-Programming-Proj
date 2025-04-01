package vandi;

public class TurboPart extends Part {
    public TurboPart(String name, int price, int performanceBoost, double weight) {
        super(name, price, performanceBoost, weight);
    }

    @Override
    public void applyUpgrade(PlayerCar car) {
        car.setAcceleration(car.getAcceleration() + getPerformanceBoost());
    }
}