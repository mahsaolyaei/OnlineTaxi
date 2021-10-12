package ir.maktab.model;

import ir.maktab.dao.DriverDao;
import ir.maktab.dao.PassengerDao;
import ir.maktab.dao.TripDao;
import ir.maktab.enums.PaymentType;
import ir.maktab.enums.TripStatus;
import ir.maktab.enums.UserStatus;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class Trip extends General {
    private Location origin;
    private Location destination;
    private Driver driver;
    private Passenger passenger;
    private BigDecimal amount;
    private TripStatus status;
    private PaymentType paymentType;
    boolean paymentConfirmed;

    public Trip() {
    }

    public Trip(Location origin, Location destination, Driver driver, Passenger passenger, BigDecimal amount, PaymentType paymentType) {
        this.origin = origin;
        this.destination = destination;
        this.driver = driver;
        this.passenger = passenger;
        this.amount = amount;
        this.paymentType = paymentType;
        this.status = TripStatus.ONGOING;
        this.paymentConfirmed = false;
    }

    public static Trip add(Location origin, Location destination, Driver driver, Passenger passenger, BigDecimal tripAmount, PaymentType paymentType) throws SQLException, ClassNotFoundException {
        Trip trip = new Trip(origin, destination, driver, passenger, tripAmount, paymentType);
        int tripId = (new TripDao()).create(trip);
        if (tripId > 0) {
            passenger.setStatus(UserStatus.TRAVELING);
            (new PassengerDao()).changeStatus(passenger);
            driver.setStatus(UserStatus.TRAVELING);
            (new DriverDao()).changeStatus(driver);
            trip.setId(tripId);
            return trip;
        }
        return null;
    }

    public static void showOnGoingTrips() throws SQLException, ClassNotFoundException {
        List<Trip> tripList = (new TripDao()).findOngoingTrips();
        if (tripList.size() == 0)
            System.out.println("There Is No OnGoing Trip.");
        for (Trip trip : tripList) {
            System.out.println(trip);
        }
    }

    public void confirmCashReceipt() throws SQLException, ClassNotFoundException {
        this.paymentConfirmed = true;
        (new TripDao()).confirmPayment(this);
    }

    public void finish() throws SQLException, ClassNotFoundException {
        this.status = TripStatus.FINISHED;
        (new TripDao()).changeStatus(this);
        driver.finishTrip(this);
        passenger.finishTrip(this);
    }

    @Override
    public String toString() {
        return "Trip{" +
                "origin=" + origin +
                ", destination=" + destination + "\n" +
                ", driver=" + driver + "\n" +
                ", passenger=" + passenger + "\n" +
                ", amount=" + getAmount() +
                ", status=" + status +
                ", paymentType=" + paymentType +
                ", paymentConfirmed=" + paymentConfirmed +
                '}';
    }

    public static BigDecimal calculateTripAmount(Location origin, Location destination) {
        return new BigDecimal(1000 * origin.calculateDistance(destination)).setScale(0, BigDecimal.ROUND_UP);
    }

    public Location getOrigin() {
        return origin;
    }

    public void setOrigin(Location origin) {
        this.origin = origin;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public BigDecimal getAmount() {
        return amount.setScale(0, BigDecimal.ROUND_UP);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public boolean isPaymentConfirmed() {
        return paymentConfirmed;
    }

    public void setPaymentConfirmed(boolean paymentConfirmed) {
        this.paymentConfirmed = paymentConfirmed;
    }
}
