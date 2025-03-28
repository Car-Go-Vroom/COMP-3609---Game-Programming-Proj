package vandi;

import java.util.ArrayList;
import java.util.List;

public class Store {
    private List<CarObject> availableCars;
    private List<Part> availableParts;

    public Store() {
        availableCars = new ArrayList<>();
        availableParts = new ArrayList<>();
    }

    public void sellCar(CarObject car) {
        // Car selling logic
    }

    public void sellPart(Part part) {
        // Part selling logic
    }
}
