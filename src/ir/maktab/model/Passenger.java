package ir.maktab.model;

import ir.maktab.dao.PassengerDao;
import ir.maktab.dao.TransactionDao;
import ir.maktab.enums.ObjectType;
import ir.maktab.enums.PaymentType;
import ir.maktab.enums.TransactionType;
import ir.maktab.enums.UserStatus;
import ir.maktab.exceptions.TripException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class Passenger extends General {

    private Person person;
    private String userName;
    private UserStatus status;
    private BigDecimal balance;

    public Passenger(Person person, String userName) {
        super();
        this.person = person;
        this.userName = userName;
        this.status = UserStatus.ABSENT;
        this.balance = new BigDecimal(0);
    }

    public Passenger() {
    }


    public static int add(Person person, String userName) throws SQLException, ClassNotFoundException {
        Passenger passenger = new Passenger(person, userName);
        return (new PassengerDao()).create(passenger);
    }

    public static boolean checkUserNameExists(String userName) throws SQLException, ClassNotFoundException {
        Passenger passenger = (new PassengerDao()).findByUserName(userName);
        return passenger == null;
    }

    public static void showPassengersList() throws SQLException, ClassNotFoundException {
        List<Passenger> passengerList = (new PassengerDao()).findAll();
        if (passengerList.size() == 0)
            System.out.println("The Passengers List Is Empty.");
        for (Passenger passenger : passengerList) {
            System.out.println(passenger);
        }
    }

    public void increaseBalance(BigDecimal amount) throws SQLException, ClassNotFoundException {
        Transaction transaction = new Transaction(amount, TransactionType.INCREASE, id, ObjectType.PASSENGER);
        (new TransactionDao()).create(transaction);
        this.balance = balance.add(amount);
        (new PassengerDao()).updateBalance(this);
    }

    public Trip requestTrip(PaymentType paymentType, Location origin, Location destination) throws SQLException, ClassNotFoundException, TripException {
        BigDecimal tripAmount = Trip.calculateTripAmount(origin, destination);
        if (paymentType.equals(PaymentType.BALANCE) && balance.compareTo(tripAmount) < 0) {
            throw new TripException(String.format("The Trip Amount [%f] Is More Than Your Balance [%f]" +
                    "\nPlease Increase Your Balance First.", tripAmount, getBalance()));
        }
        Driver nearestDriver = Driver.findNearestWaitingDriver(origin);
        if (nearestDriver == null)
            throw new TripException("There Is No Driver.");
        Trip trip = Trip.add(origin, destination, nearestDriver, this, tripAmount, paymentType);
        return trip;
    }

    public void finishTrip(Trip trip) throws SQLException, ClassNotFoundException {
        this.status = UserStatus.ABSENT;
        if (trip.getPaymentType().equals(PaymentType.BALANCE)) {
            BigDecimal amount = trip.getAmount();
            Transaction transaction = new Transaction(amount, TransactionType.DECREASE, id, ObjectType.PASSENGER);
            (new TransactionDao()).create(transaction);
            this.balance = this.balance.subtract(amount);
        }
        (new PassengerDao()).changeTripFinishingFields(this);
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "person=" + person +
                ", status=" + status +
                ", balance=" + getBalance() +
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
        return balance.setScale(0, BigDecimal.ROUND_UP);
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
