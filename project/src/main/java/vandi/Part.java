package vandi;

public class Part {
    private String type;
    private String name;
    private int price;
    private int performanceBoost;
    private double weight;

    public Part(String type, String name, int price,
            int performanceBoost, double weight) {
        this.type = type;
        this.name = name;
        this.price = price;
        this.performanceBoost = performanceBoost;
        this.weight = weight;
    }
}