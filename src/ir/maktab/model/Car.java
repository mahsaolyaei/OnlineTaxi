package ir.maktab.model;

import ir.maktab.dao.VehicleDao;

import java.sql.SQLException;

public class Car extends Vehicle{

    public Car() {
    }

    public Car(String number, String model, String color) {
        super(number, model, color);
        super.setType(VehicleType.CAR);
    }

    public static Car add(String number, String model, String color) throws SQLException, ClassNotFoundException {
        Car car = new Car(number, model, color);
        int vehicleId = (new VehicleDao()).create(car);
        if (vehicleId > 0) {
            car.setId(vehicleId);
            return car;
        }
        return null;
    }
}
