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
        if (garage.getSelectedPlayerCar() != null) {
            return garage.getSelectedPlayerCar();
        }
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

    public void selectCarFromGarage(PlayerCar car) {
        garage.setSelectedPlayerCar(car);
    }

    public Garage getGarage() {
        return garage;
    }
}