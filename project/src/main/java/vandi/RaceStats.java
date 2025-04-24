package vandi;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RaceStats {
    private int racesWon;
    private int racesLost;
    private int timesArrested;
    private double totalMoneyEarned;
    private double totalMoneyLost;
    private int carsLost;

    public RaceStats() {
        racesWon = 0;
        racesLost = 0;
        timesArrested = 0;
        totalMoneyEarned = 0;
        totalMoneyLost = 0;
        carsLost = 0;
    }
    
    // Getters and Setters (in case Lombok doesn't work)
    public int getRacesWon() {
        return racesWon;
    }
    
    public void setRacesWon(int racesWon) {
        this.racesWon = racesWon;
    }
    
    public int getRacesLost() {
        return racesLost;
    }
    
    public void setRacesLost(int racesLost) {
        this.racesLost = racesLost;
    }
    
    public int getTimesArrested() {
        return timesArrested;
    }
    
    public void setTimesArrested(int timesArrested) {
        this.timesArrested = timesArrested;
    }
    
    public double getTotalMoneyEarned() {
        return totalMoneyEarned;
    }
    
    public void setTotalMoneyEarned(double totalMoneyEarned) {
        this.totalMoneyEarned = totalMoneyEarned;
    }
    
    public double getTotalMoneyLost() {
        return totalMoneyLost;
    }
    
    public void setTotalMoneyLost(double totalMoneyLost) {
        this.totalMoneyLost = totalMoneyLost;
    }
    
    public int getCarsLost() {
        return carsLost;
    }
    
    public void setCarsLost(int carsLost) {
        this.carsLost = carsLost;
    }
}