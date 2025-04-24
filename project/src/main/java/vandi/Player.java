package vandi;

public class Player {
    private int money;
    private Garage garage;
    private RaceStats raceStats;

    public Player() {
        money = 5000; // Starting money
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
    
    public void addMoney(int amount) {
        this.money += amount;
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
    
    public RaceStats getRaceStats() {
        return raceStats;
    }
    
    public void updateRaceStats(boolean won, double moneyEarned, double moneyLost) {
        if (won) {
            raceStats.setRacesWon(raceStats.getRacesWon() + 1);
            raceStats.setTotalMoneyEarned(raceStats.getTotalMoneyEarned() + moneyEarned);
        } else {
            raceStats.setRacesLost(raceStats.getRacesLost() + 1);
            raceStats.setTotalMoneyLost(raceStats.getTotalMoneyLost() + moneyLost);
        }
    }
}