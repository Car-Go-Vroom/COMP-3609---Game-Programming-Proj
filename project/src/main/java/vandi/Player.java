package vandi;

public class Player {
    private int money;
    private Garage garage;
    private RaceStats raceStats;

    public Player() {
        garage = new Garage();
        raceStats = new RaceStats();
    }

    public CarObject selectCar() {
        // Car selection logic
        return null;
    }

    public int getMoney() {
        return money;
    }

    public void deductMoney(int amount) {
        this.money -= amount;
    }

    public void removeCar(PlayerCar car) {
        garage.removeCar(car);
    }
}