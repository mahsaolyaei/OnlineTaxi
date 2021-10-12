package ir.maktab.model;

import ir.maktab.dao.DriverDao;
import ir.maktab.dao.PersonDao;
import ir.maktab.dao.VehicleDao;

import java.sql.SQLException;

public class Person extends General{
    private String firstName;
    private String lastName;
    private String nationCode;

    public Person(String firstName, String lastName, String nationCode) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationCode = nationCode;
    }

    public Person() {

    }

    public static Person add(String firstName, String lastName, String nationCode) throws SQLException, ClassNotFoundException {
        PersonDao personDao = new PersonDao();
        Person person = personDao.findByNationCode(nationCode);
        if (person != null) {
            System.out.printf("A Person With NationCode [%s] Exists. So We Did Not Create New Person.\n", nationCode);
            return person;
        }
        person = new Person(firstName, lastName, nationCode);
        int personId = (new PersonDao()).create(person);
        if (personId > 0) {
            person.setId(personId);
            return person;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", nationCode='" + nationCode + '\'' +
                '}';
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
}
