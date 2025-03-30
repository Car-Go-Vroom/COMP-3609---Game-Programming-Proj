package vandi;

public class Police extends CarObject {
    private boolean active;
    private double pursuitProbability;
    private int currentLevel;
    private double playerTopSpeed;

    public Police() {
        super();
    }

    /**
     * @param targetX
     * @param targetY
     *                The parameters represent the player position.
     */
    public void chase(double targetX, double targetY, Player player, PlayerCar playerCar) {
        double pursuitSpeed = getVelocity() * 1.2;
        double maxPursuitDistance = 300;
        double catchDistance = 50;
        double policeX = getPositionX();
        double policeY = getPositionY();
        double deltaX = targetX - policeX;
        double deltaY = targetY - policeY;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        if (distance <= maxPursuitDistance) {
            double directionX = deltaX / distance;
            double directionY = deltaY / distance;
            setPositionX(policeX + directionX * pursuitSpeed);
            setPositionY(policeY + directionY * pursuitSpeed);
            if (distance <= catchDistance) {
                arrestPlayer(player, playerCar);
            }
        }
    }

    public void arrestPlayer(Player player, PlayerCar car) {
        if (!active)
            return;
        int baseFine = 1000;
        int levelMultiplier = currentLevel;
        int totalFine = (int) (baseFine * levelMultiplier);
        // Check if player can pay fine
        if (player.getMoney() >= totalFine) {
            takeMoney(player, totalFine);
        } else {
            // If player can't pay, confiscate car
            takeCar(player, car);
        }
        setActive(false);
    }

    private void takeMoney(Player player, int amount) {
        player.deductMoney(amount);
    }

    private void takeCar(Player player, PlayerCar car) {
        player.removeCar(car);
    }

    public void takeMoney(int amount) {
        // Money taking logic
    }

    public void takeCar() {
        // Car confiscation logic
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setCurrentLevel(int level) {
        this.currentLevel = level;
    }

    public void setPlayerTopSpeed(double speed) {
        this.playerTopSpeed = speed;
    }
}
