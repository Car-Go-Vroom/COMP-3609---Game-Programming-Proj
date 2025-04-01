package vandi;

public class NitroPart extends Part {
    public NitroPart(String name, int price, int performanceBoost, double weight) {
        super(name, price, performanceBoost, weight);
    }

    @Override
    public void applyUpgrade(PlayerCar car) {
        car.setNitroAmount(car.getNitroAmount() + getPerformanceBoost());
    }
}