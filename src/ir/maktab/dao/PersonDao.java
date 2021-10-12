package ir.maktab.dao;

import ir.maktab.model.Driver;
import ir.maktab.model.Person;
import ir.maktab.model.Vehicle;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonDao extends GeneralDao {
    public PersonDao() throws SQLException, ClassNotFoundException {
    }

    @Override
    public int create(Object object) throws SQLException {
        try {
            Person person = (Person) object;
            String statement = String.format("Insert Into Tb_Person(first_name, last_name, nation_code) " +
                    "values('%s', '%s', '%s')", person.getFirstName(), person.getLastName(), person.getNationCode());
            return executeCreate(statement);
        } catch (ClassCastException ex) {
            System.out.println("The Object Is Not Person.");
        }
        return 0;
    }

    public Person findByNationCode(String nationCode) throws SQLException, ClassNotFoundException {
        String statement = String.format("Select * From Tb_person where nation_code = '%s'", nationCode);
        ResultSet resultSet = executeQuery(statement);
        if (resultSet.next())
            return fillData(resultSet);
        return null;
    }

    @Override
    protected Person fillData(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        return fillData(resultSet, "");
    }

    public Person fillData(ResultSet resultSet, String extraString) throws SQLException {
        Person person = new Person();
        int personId;
        try {
            personId = resultSet.getInt(extraString  + "person_id");
        }
        catch (SQLException ex) {
            personId = resultSet.getInt("id");
        }
        person.setId(personId);
        person.setFirstName(resultSet.getString(extraString + "first_name"));
        person.setLastName(resultSet.getString(extraString + "last_name"));
        person.setNationCode(resultSet.getString(extraString + "nation_code"));
        return person;
    }
}
