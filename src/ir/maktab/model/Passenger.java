package ir.maktab.model;

import ir.maktab.dao.PassengerDao;

import java.sql.SQLException;

public class Passenger extends General {

    private String userName;
    private String firstName;
    private String lastName;
    private String nationCode;
    private PassengerStatus status;

    public Passenger(String userName, String firstName, String lastName, String nationCode) {
        super();
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationCode = nationCode;
        this.status = PassengerStatus.ABSENT;
    }

    public Passenger() {
    }


    public static int add(String userName, String firstName, String lastName, String nationCode) throws SQLException, ClassNotFoundException {
        Passenger passenger = new Passenger(userName, firstName, lastName, nationCode);
        return (new PassengerDao()).create(passenger);
    }

    public static boolean checkUserNameExists(String userName) throws SQLException, ClassNotFoundException {
        Passenger passenger = (new PassengerDao()).findByUserName(userName);
        return passenger == null? true: false;
    }

    public static void showPassengersList() throws SQLException, ClassNotFoundException {
        Passenger[] passengers = (new PassengerDao()).findAll();
        if (passengers.length == 0)
            System.out.println("The Passengers List Is Empty.");
        for (int i = 0; i < passengers.length; i++) {
            System.out.println(passengers[i]);
        }
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", nationCode='" + nationCode + '\'' +
                ", status='" + status + '\'' +
                '}';
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

    public PassengerStatus getStatus() {
        return status;
    }

    public void setStatus(PassengerStatus status) {
        this.status = status;
    }
}
