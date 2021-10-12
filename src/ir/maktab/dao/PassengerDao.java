package ir.maktab.dao;

import ir.maktab.model.Driver;
import ir.maktab.model.Passenger;
import ir.maktab.model.PassengerStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

public class PassengerDao extends GeneralDao {

    public PassengerDao() throws SQLException, ClassNotFoundException {
        super();
    }

    @Override
    public int create(Object object) throws SQLException {
        try {
            Passenger passenger = (Passenger) object;
            String statement = String.format("Insert Into Tb_Passenger(user_name, first_name, last_name, nation_code, passenger_status) " +
                    "values('%s', '%s', '%s', '%s', '%s')", passenger.getUserName(), passenger.getFirstName(), passenger.getLastName(), passenger.getNationCode(), passenger.getStatus());
            return executeUpdate(statement);
        } catch (ClassCastException ex) {
            System.out.println("The Object Is Not Passenger.");
        }
        return 0;
    }

    @Override
    public Passenger[] findAll() throws SQLException {
        String statement = String.format("Select * From Tb_Passenger");
        ResultSet resultSet = executeQuery(statement);
        int rowCount = findRowCount(resultSet);
        Passenger[] passengers = new Passenger[rowCount];
        for (int i = 0; i < rowCount; i++) {
            if(resultSet.next())
                passengers[i] = fillData(resultSet);
        }
        return passengers;
    }

    public Passenger findByUserName(String userName) throws SQLException {
        String statement = String.format("Select * From Tb_Passenger where lower(user_name) = '%s'", userName.toLowerCase());
        ResultSet resultSet = executeQuery(statement);
        if (resultSet.next())
            return fillData(resultSet);
        return null;
    }

    @Override
    protected Passenger fillData(ResultSet resultSet) throws SQLException {
        Passenger passenger = new Passenger();
        passenger.setId(resultSet.getInt("id"));
        passenger.setUserName(resultSet.getString("user_name"));
        passenger.setFirstName(resultSet.getString("first_name"));
        passenger.setLastName(resultSet.getString("last_name"));
        passenger.setNationCode(resultSet.getString("nation_code"));
        passenger.setStatus(PassengerStatus.valueOf(resultSet.getString("passenger_status")));
        return passenger;
    }
}
