package ir.maktab.model;

import ir.maktab.dao.DriverDao;

import java.sql.SQLException;

public class Driver extends General{

    private String userName;
    private String firstName;
    private String lastName;
    private String nationCode;
    private Vehicle vehicle;

    public Driver(String userName, String firstName, String lastName, String nationCode, Vehicle vehicle) {
        super();
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationCode = nationCode;
        this.vehicle = vehicle;
    }

    public Driver() {
    }

    public static int add(String userName, String firstName, String lastName, String nationCode, Vehicle vehicle) throws SQLException, ClassNotFoundException {
        Driver driver = new Driver(userName, firstName, lastName, nationCode, vehicle);
        return (new DriverDao()).create(driver);
    }

    public static boolean checkUserNameExists(String userName) throws SQLException, ClassNotFoundException {
        Driver driver = (new DriverDao()).findByUserName(userName);
        return driver == null? true: false;
    }

    public static void showDriversList() throws SQLException, ClassNotFoundException {
        Driver[] drivers = (new DriverDao()).findAll();
        if (drivers.length == 0)
            System.out.println("The Drivers List Is Empty.");
        for (int i = 0; i < drivers.length; i++) {
            System.out.println(drivers[i]);
        }
    }

    @Override
    public String toString() {
        return "Driver{" +
                "userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", nationCode='" + nationCode + '\'' +
                ", vehicle=" + vehicle.toString() +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationCode() {
        return nationCode;
    }

    public void setNationCode(String nationCode) {
        this.nationCode = nationCode;
    }

    public Vehicle getVehicle() {
        if (vehicle == null)
            vehicle = new Vehicle();
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

}
