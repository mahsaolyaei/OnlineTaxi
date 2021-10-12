package ir.maktab;

import ir.maktab.dao.*;
import ir.maktab.enums.*;
import ir.maktab.exceptions.TripException;
import ir.maktab.model.*;

import java.sql.SQLException;
import java.util.*;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        do {
            System.out.println("\n***Welcome To Online Taxi Service***\n" +
                    "1) Add A Group Of Drivers\n" +
                    "2) Add A Group Of Passengers\n" +
                    "3) Driver signup or login\n" +
                    "4) Passenger signup or login\n" +
                    "5) Show Ongoing Travels\n" +
                    "6) Show A List Of Drivers\n" +
                    "7) Show A List Of Passengers\n" +
                    "9) Exit\n");
            String inputStr = SCANNER.next();
            if ("1".equals(inputStr))
                goToDriversGroupAdd();
            else if ("2".equals(inputStr))
                goToPassengersGroupAdd();
            else if ("3".equals(inputStr))
                goToLoginMenu("DRIVER");
            else if ("4".equals(inputStr))
                goToLoginMenu("PASSENGER");
            else if ("5".equals(inputStr))
                Trip.showOnGoingTrips();
            else if ("6".equals(inputStr))
                Driver.showDriversList();
            else if ("7".equals(inputStr))
                Passenger.showPassengersList();
            else if ("9".equals(inputStr))
                break;
            else
                System.out.println("[ERROR] You Entered Invalid Number.");
        } while (true);
    }

    private static void goToLoginMenu(String type) throws SQLException, ClassNotFoundException {
        System.out.println("Please Enter Your UserName: ");
        String userName = SCANNER.next();

        if (type.equalsIgnoreCase("DRIVER")) {
            Driver driver = (new DriverDao()).findByUserName(userName.trim());
            if (driver != null)
                goToDriverMenu(driver);
            else
                goToSignupMenu(type, userName);
        }
        else if (type.equalsIgnoreCase("PASSENGER")) {
            Passenger passenger = (new PassengerDao()).findByUserName(userName.trim());
            if (passenger != null)
                goToPassengerMenu(passenger);
            else
                goToSignupMenu(type, userName);
        }

    }

    private static void goToPassengerMenu(Passenger passenger) throws SQLException, ClassNotFoundException {
        do {
            if (passenger.getStatus().equals(UserStatus.TRAVELING)) {
                System.out.println("***You Are In A Travel***\n" +
                        "1) Exit\n");
                String inputStr = SCANNER.next();
                if ("1".equals(inputStr))
                    break;
                else
                    System.out.println("[ERROR] You Entered Invalid Number.");
            }
            else if (!passenger.getStatus().equals(UserStatus.WAITING)) {
                System.out.println("***You Are Not Waiting For A Travel***\n" +
                        "1) Travel Request (Pay By Cash)\n" +
                        "2) Travel Request (Pay By Account Balance)\n" +
                        "3) Increase Account Balance\n" +
                        "4) Exit\n");
                String inputStr = SCANNER.next();
                if ("1".equals(inputStr))
                    MainHelper.requestTrip(passenger, PaymentType.CASH);
                else if ("2".equals(inputStr))
                    MainHelper.requestTrip(passenger, PaymentType.BALANCE);
                else if ("3".equals(inputStr))
                    MainHelper.increaseBalance(passenger);
                else if ("4".equals(inputStr))
                    break;
                else
                    System.out.println("[ERROR] You Entered Invalid Number.");

            }
        }
        while(true);
    }

    private static void goToSignupMenu(String type, String userName) {
        System.out.println("\n******\n" +
                "1) Register\n" +
                "2) Exit\n");
        String inputStr = SCANNER.next();
        if ("1".equals(inputStr)) {
            if (type.equalsIgnoreCase("DRIVER"))
                MainHelper.addDrivers(1, userName);
            else if (type.equalsIgnoreCase("PASSENGER"))
                MainHelper.addPassengers(1, userName);
        }
        else if ("2".equals(inputStr))
            return;
        else
            System.out.println("[ERROR] You Entered Invalid Number.");
    }

    private static void goToDriverMenu(Driver driver) throws SQLException, ClassNotFoundException {
        do {
            if (driver.getStatus().equals(UserStatus.WAITING)) {
                System.out.println("***You Are Waiting For A Travel***\n" +
                        "1) Exit\n");
                String inputStr = SCANNER.next();
                if ("1".equals(inputStr))
                    break;
                else
                    System.out.println("[ERROR] You Entered Invalid Number.");
            } else if (driver.getStatus().equals(UserStatus.TRAVELING)) {
                try {
                    Trip trip = (new TripDao()).findOnGoingTripByDriver(driver);
                    System.out.println("***You Are In A Travel***\n" +
                            (trip.getPaymentType().equals(PaymentType.CASH) && !trip.isPaymentConfirmed()? "1) Confirm Cash Receipt\n": "") +
                            "2) Travel Finished\n" +
                            "3) Exit\n");
                    String inputStr = SCANNER.next();
                    if ("1".equals(inputStr))
                        trip.confirmCashReceipt();
                    else if ("2".equals(inputStr)) {
                        MainHelper.finishTrip(trip);
                        driver.setStatus(UserStatus.WAITING);
                    }
                    else if ("3".equals(inputStr))
                        break;
                    else
                        System.out.println("[ERROR] You Entered Invalid Number.");
                } catch (TripException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } while (true);
    }

    private static void goToPassengersGroupAdd() {
        System.out.println("Please Enter The Count Of Passengers Do You Want To Add: ");
        int count;
        try {
            count = SCANNER.nextInt();
        }
        catch(InputMismatchException ex) {
            System.out.println("You Must Enter A Number.");
            return;
        }
        MainHelper.addPassengers(count, null);
    }

    private static void goToDriversGroupAdd() {
        System.out.println("Please Enter The Count Of Drivers Do You Want To Add: ");
        int count;
        try {
            count = SCANNER.nextInt();
        }
        catch(InputMismatchException ex) {
            System.out.println("You Must Enter A Number.");
            return;
        }
        MainHelper.addDrivers(count, null);

    }
}
