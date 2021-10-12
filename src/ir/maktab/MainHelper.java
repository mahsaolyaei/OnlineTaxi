package ir.maktab;

import ir.maktab.enums.*;
import ir.maktab.exceptions.*;
import ir.maktab.model.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

public class MainHelper {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void requestTrip(Passenger passenger, PaymentType paymentType) throws SQLException, ClassNotFoundException {
        try {
            System.out.println("Please Enter The Origin Of Your Trip In This Order: [longitude latitude]");
            Location origin = new Location(SCANNER.nextInt(), SCANNER.nextInt());
            System.out.println("Please Enter The Destination Of Your Trip In This Order: [longitude latitude]");
            Location destination = new Location(SCANNER.nextInt(), SCANNER.nextInt());
            Trip newTrip = passenger.requestTrip(paymentType, origin, destination);
            if (newTrip != null)
                System.out.println("The Trip Is Started.");
        } catch (InputMismatchException ex) {
            System.out.println("You Must Enter Number Values.");
        } catch (TripException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void increaseBalance(Passenger passenger) throws SQLException, ClassNotFoundException {
        try {
            System.out.println("Please Enter The Amount You Want To Add To Your Balance: ");
            BigDecimal inputAmount = SCANNER.nextBigDecimal();
            passenger.increaseBalance(inputAmount);
            System.out.printf("Your New Balance Is %f T\n", passenger.getBalance());
        } catch (InputMismatchException ex) {
            System.out.println("You Must Enter A Number Value.");
        }
    }

    public static void finishTrip(Trip trip) throws TripException, SQLException, ClassNotFoundException {
        if (trip.getPaymentType().equals(PaymentType.CASH) && !trip.isPaymentConfirmed())
            throw new TripException("You Must Confirm Payment Before Finish Trip");
        trip.finish();
    }

    public static void addPassengers(int count, String userName) {
        try {
            for (int i = 0; i < count; i++) {
                System.out.println("\nPlease Enter Passenger Info In This Order: \n" +
                        "[" + (userName == null ? "UserName, " : "") + "FirstName, LastName, NationCode]\n");
//                if (i == 0)
//                    SCANNER.nextLine();
                String inputStr = SCANNER.nextLine();
                StringTokenizer inputTokenizer = new StringTokenizer(inputStr, ",");
                if (userName == null)
                    userName = inputTokenizer.hasMoreTokens() ? inputTokenizer.nextToken() : null;
                String firstName = inputTokenizer.hasMoreTokens() ? inputTokenizer.nextToken() : null;
                String lastName = inputTokenizer.hasMoreTokens() ? inputTokenizer.nextToken() : null;
                String nationCode = inputTokenizer.hasMoreTokens() ? inputTokenizer.nextToken() : null;
                if (userName == null || firstName == null || lastName == null || nationCode == null) {
                    System.out.println("You Entered Passenger Info In Wrong Format.");
                    continue;
                }
                userName = userName.trim();
                if (!Passenger.checkUserNameExists(userName)) {
                    System.out.printf("The UserName [%s] Already Exists.\n", userName);
                    continue;
                }
                Person person = Person.add(firstName.trim(), lastName.trim(), nationCode.trim());
                Passenger.add(person, userName);
                System.out.printf("The Passenger With UserName [%s] Added.\n", userName);
            }
        } catch (Exception ex) {
            System.out.println("Unexpected Error Occurred");
            ex.printStackTrace();
        }
    }

    public static void addDrivers(int count, String userName) {
        try {
            for (int i = 0; i < count; i++) {
                System.out.println("\nPlease Enter Driver Info In This Order: \n" +
                        "[" + (userName == null ? "UserName, " : "") + "FirstName, LastName, NationCode]\n");
//                if (i == 0) {
//                    SCANNER.nextLine();
//                    SCANNER.nextLine();
//                }
                String inputStr = SCANNER.nextLine();
                StringTokenizer inputTokenizer = new StringTokenizer(inputStr, ",");
                if (userName == null)
                    userName = inputTokenizer.hasMoreTokens() ? inputTokenizer.nextToken() : null;
                String firstName = inputTokenizer.hasMoreTokens() ? inputTokenizer.nextToken() : null;
                String lastName = inputTokenizer.hasMoreTokens() ? inputTokenizer.nextToken() : null;
                String nationCode = inputTokenizer.hasMoreTokens() ? inputTokenizer.nextToken() : null;
                if (userName == null || firstName == null || lastName == null || nationCode == null) {
                    System.out.println("You Entered Driver Info In Wrong Format.");
                    return;
                }
                userName = userName.trim();
                if (!Driver.checkUserNameExists(userName)) {
                    System.out.printf("The UserName [%s] Already Exists.\n", userName);
                }
                System.out.println("Please Enter Driver's Car Info In This Order: \n" +
                        "[CarNumber, Model, Color]\n");
                inputStr = SCANNER.nextLine();
                inputTokenizer = new StringTokenizer(inputStr, ",");
                String number = inputTokenizer.hasMoreTokens() ? inputTokenizer.nextToken() : null;
                String model = inputTokenizer.hasMoreTokens() ? inputTokenizer.nextToken() : null;
                String color = inputTokenizer.hasMoreTokens() ? inputTokenizer.nextToken() : null;
                if (number == null || model == null || color == null) {
                    System.out.println("You Entered Car Info In Wrong Format.");
                    return;
                }
                System.out.println("Please Enter The Driver's Location In This Order: [longitude latitude]");
                Location location = new Location(SCANNER.nextInt(), SCANNER.nextInt());
                Car car = Car.add(number.trim(), model.trim(), color.trim());
                Person person = Person.add(firstName.trim(), lastName.trim(), nationCode.trim());
                Driver.add(person, userName, car, location);
                System.out.printf("The Driver With UserName [%s] Added.\n", userName);
            }
        } catch (Exception ex) {
            System.out.println("Unexpected Error Occurred");
            ex.printStackTrace();
        }
    }
}
