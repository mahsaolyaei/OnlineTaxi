package ir.maktab.dao;

import ir.maktab.enums.UserStatus;
import ir.maktab.model.Driver;
import ir.maktab.model.Passenger;
import ir.maktab.enums.TripStatus;
import ir.maktab.model.Person;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PassengerDao extends GeneralDao {

    public PassengerDao() throws SQLException, ClassNotFoundException {
        super();
    }

    @Override
    public int create(Object object) throws SQLException {
        try {
            Passenger passenger = (Passenger) object;
            String statement = String.format("Insert Into Tb_Passenger(person_id, passenger_user_name, passenger_status, passenger_balance) " +
                    "values(%d, '%s', '%s', %f)", passenger.getPerson().getId(), passenger.getUserName(),
                    passenger.getStatus(), passenger.getBalance());
            return executeCreate(statement);
        } catch (ClassCastException ex) {
            System.out.println("The Object Is Not Passenger.");
        }
        return 0;
    }

    @Override
    public List findAll() throws SQLException, ClassNotFoundException {
        String statement = "Select * From Vw_Passenger";
        ResultSet resultSet = executeQuery(statement);
        List<Passenger> passengerList = new ArrayList<>();
        while(resultSet.next()) {
            passengerList.add(fillData(resultSet));
        }
        return passengerList;
    }

    public int updateBalance(Object object) throws SQLException {
        try {
            Passenger passenger = (Passenger) object;
            String statement = String.format("Update Tb_Passenger Set passenger_balance = " +
                    "%f Where Id = %d", passenger.getBalance(), passenger.getId());
            return executeUpdate(statement);
        } catch (ClassCastException ex) {
            System.out.println("The Object Is Not Passenger.");
        }
        return 0;
    }

    public Passenger findByUserName(String userName) throws SQLException, ClassNotFoundException {
        String statement = String.format("Select * From Vw_Passenger where lower(passenger_user_name) = '%s'", userName.toLowerCase());
        ResultSet resultSet = executeQuery(statement);
        if (resultSet.next())
            return fillData(resultSet);
        return null;
    }

    public int changeTripFinishingFields(Passenger passenger) throws SQLException {
        String statement = String.format("Update Tb_Passenger Set passenger_status = '%s', passenger_balance = %f" +
                        " Where Id = %d", passenger.getStatus(), passenger.getBalance(), passenger.getId());
        return executeUpdate(statement);
    }

    public int changeStatus(Passenger passenger) throws SQLException {
        String statement = String.format("Update Tb_Passenger Set passenger_status = " +
                "'%s' Where Id = %d", passenger.getStatus(), passenger.getId());
        return executeUpdate(statement);
    }

    @Override
    protected Passenger fillData(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        return fillData(resultSet, false);
    }

    public Passenger fillData(ResultSet resultSet, boolean hasSpecialPerson) throws SQLException, ClassNotFoundException {
        Passenger passenger = new Passenger();
        int passengerId;
        try {
            passengerId = resultSet.getInt("passenger_id");
        }
        catch (SQLException ex) {
            passengerId = resultSet.getInt("id");
        }
        passenger.setId(passengerId);
        Person person = (new PersonDao()).fillData(resultSet,hasSpecialPerson? "passenger_": "");
        passenger.setPerson(person);
        passenger.setUserName(resultSet.getString("passenger_user_name"));
        passenger.setBalance(resultSet.getBigDecimal("passenger_balance"));
        passenger.setStatus(UserStatus.valueOf(resultSet.getString("passenger_status")));
        return passenger;
    }
}
