package ir.maktab;

import ir.maktab.model.Car;
import ir.maktab.model.Driver;
import ir.maktab.model.Passenger;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {
    private static Scanner SCANNER = new Scanner(System.in);

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
                goToRegisterMenu("DRIVER");
            else if ("4".equals(inputStr))
                goToRegisterMenu("PASSENGER");
            else if ("5".equals(inputStr))
                System.out.println("There Is No Ongoing Travel.");
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

    private static void goToRegisterMenu(String type) throws SQLException, ClassNotFoundException {
        System.out.println("Please Enter Your UserName: ");
        String userName = SCANNER.next();
        if (!Driver.checkUserNameExists(userName)) {
            System.out.printf("The UserName [%s] Already Exists.", userName);
            return;
        }
        System.out.println("\n******\n" +
            "1) Register\n" +
            "2) Exit\n");
        String inputStr = SCANNER.next();
        if ("1".equals(inputStr)) {
            if (type.equalsIgnoreCase("DRIVER"))
                addDrivers(1);
            else if (type.equalsIgnoreCase("PASSENGER"))
                addPassengers(1);
        }
        else if ("2".equals(inputStr))
            return;
        else
            System.out.println("[ERROR] You Entered Invalid Number.");
    }

    private static void goToPassengersGroupAdd() {
        System.out.println("Please Enter The Count Of Passengers Do You Want To Add: ");
        int count = 0;
        try {
            count = SCANNER.nextInt();
        }
        catch(InputMismatchException ex) {
            System.out.println("You Must Enter A Number.");
            return;
        }
        addPassengers(count);
    }

    private static void addPassengers(int count) {
        try {
            for (int i = 0; i < count; i++) {
                System.out.println("\nPlease Enter Passenger Info In This Order: \n" +
                        "[UserName, FirstName, LastName, NationCode]\n");
                if (i == 0)
                    SCANNER.nextLine();
                String inputStr = SCANNER.nextLine();
                StringTokenizer inputTokenizer = new StringTokenizer(inputStr, ",");
                String userName = inputTokenizer.hasMoreTokens()? inputTokenizer.nextToken(): null;
                String firstName = inputTokenizer.hasMoreTokens()? inputTokenizer.nextToken(): null;
                String lastName = inputTokenizer.hasMoreTokens()? inputTokenizer.nextToken(): null;
                String nationCode = inputTokenizer.hasMoreTokens()? inputTokenizer.nextToken(): null;
                if (userName == null || firstName == null || lastName == null || nationCode == null) {
                    System.out.println("You Entered Passenger Info In Wrong Format.");
                    continue;
                }
                userName = userName.trim();
                if (!Passenger.checkUserNameExists(userName)) {
                    System.out.printf("The UserName [%s] Already Exists.\n", userName);
                    continue;
                }
                Passenger.add(userName, firstName.trim(), lastName.trim(), nationCode.trim());
                System.out.printf("The Passenger With UserName [%s] Added.\n", userName);
            }
        } catch (Exception ex) {
            System.out.println("Unexpected Error Occurred");
        }
    }

    private static void goToDriversGroupAdd() {
        System.out.println("Please Enter The Count Of Drivers Do You Want To Add: ");
        int count = 0;
        try {
            count = SCANNER.nextInt();
        }
        catch(InputMismatchException ex) {
            System.out.println("You Must Enter A Number.");
            return;
        }
        addDrivers(count);

    }

    private static void addDrivers(int count) {
        try {
            for (int i = 0; i < count; i++) {
                System.out.println("\nPlease Enter Driver Info In This Order: \n" +
                        "[UserName, FirstName, LastName, NationCode]\n");
                if (i == 0)
                    SCANNER.nextLine();
                String inputStr = SCANNER.nextLine();
                StringTokenizer inputTokenizer = new StringTokenizer(inputStr, ",");
                String userName = inputTokenizer.hasMoreTokens()? inputTokenizer.nextToken(): null;
                String firstName = inputTokenizer.hasMoreTokens()? inputTokenizer.nextToken(): null;
                String lastName = inputTokenizer.hasMoreTokens()? inputTokenizer.nextToken(): null;
                String nationCode = inputTokenizer.hasMoreTokens()? inputTokenizer.nextToken(): null;
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
                String number = inputTokenizer.hasMoreTokens()? inputTokenizer.nextToken(): null;
                String model = inputTokenizer.hasMoreTokens()? inputTokenizer.nextToken(): null;
                String color = inputTokenizer.hasMoreTokens()? inputTokenizer.nextToken(): null;
                if (number == null || model == null || color == null) {
                    System.out.println("You Entered Car Info In Wrong Format.");
                    return;
                }
                Car car = Car.add(number.trim(), model.trim(), color.trim());
                Driver.add(userName, firstName.trim(), lastName.trim(), nationCode.trim(), car);
                System.out.printf("The Driver With UserName [%s] Added.\n", userName);
            }
        } catch (Exception ex) {
            System.out.println("Unexpected Error Occurred");
        }
    }
}
