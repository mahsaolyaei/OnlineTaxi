package ir.maktab.model;

import ir.maktab.dao.DriverDao;
import ir.maktab.dao.TransactionDao;
import ir.maktab.enums.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class Driver extends General {

    private Person person;
    private String userName;
    private UserStatus status;
    private BigDecimal balance;
    private Vehicle vehicle;
    private Location location;

    public Driver(Person person, String userName, Vehicle vehicle, Location location) {
        super();
        this.person = person;
        this.userName = userName;
        this.vehicle = vehicle;
        this.location = location;
        this.status = UserStatus.WAITING;
        this.balance = new BigDecimal(0);
    }

    public Driver() {
    }

    public static int add(Person person, String userName, Vehicle vehicle, Location location) throws SQLException, ClassNotFoundException {
        Driver driver = new Driver(person, userName, vehicle, location);
        return (new DriverDao()).create(driver);
    }

    public static boolean checkUserNameExists(String userName) throws SQLException, ClassNotFoundException {
        Driver driver = (new DriverDao()).findByUserName(userName);
        return driver == null;
    }

    public static void showDriversList() throws SQLException, ClassNotFoundException {
        List<Driver> driverList = (new DriverDao()).findAll();
        if (driverList.size() == 0)
            System.out.println("The Passengers List Is Empty.");
        for (Driver driver : driverList) {
            System.out.println(driver);
        }
    }

    public static Driver findNearestWaitingDriver(Location origin) throws SQLException, ClassNotFoundException {
        List<Driver> driverList = (new DriverDao()).findAllWaiting();
        Driver nearestDriver = null;
        Double minimumDistance = null;
        for (Driver driver : driverList) {
            Double distance = origin.calculateDistance(driver.getLocation());
            if (nearestDriver == null || distance.compareTo(minimumDistance) < 0) {
                minimumDistance = distance;
                nearestDriver = driver;
            }
        }
        return nearestDriver;
    }

    public void finishTrip(Trip trip) throws SQLException, ClassNotFoundException {
        this.status = UserStatus.WAITING;
        this.location = trip.getDestination();
        if (trip.getPaymentType().equals(PaymentType.BALANCE)) {
            BigDecimal amount = trip.getAmount();
            Transaction transaction = new Transaction(amount, TransactionType.INCREASE, id, ObjectType.DRIVER);
            (new TransactionDao()).create(transaction);
            this.balance = balance.add(trip.getAmount());
        }
        (new DriverDao()).changeTripFinishingFields(this);
    }

    @Override
    public String toString() {
        return "Driver{" +
                "person=" + person +
                ", status=" + status +
                ", balance=" + getBalance() +
                ", vehicle=" + vehicle +
                ", location=" + location +
                '}';
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Vehicle getVehicle() {
        if (vehicle == null)
            vehicle = new Vehicle();
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
