package ir.maktab.model;

public class Vehicle extends General{

    protected String number;
    protected String model;
    protected String color;
    private VehicleType type;

    public Vehicle() {
    }

    public Vehicle(String number, String model, String color) {
        super();
        this.number = number;
        this.model = model;
        this.color = color;
    }

    @Override
    public String toString() {
        return type + "{" +
                "number='" + number + '\'' +
                ", model='" + model + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }
}
