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
}