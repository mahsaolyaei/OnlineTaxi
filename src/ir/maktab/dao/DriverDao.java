package ir.maktab.dao;

import ir.maktab.model.Driver;
import ir.maktab.model.Vehicle;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DriverDao extends GeneralDao {

    public DriverDao() throws SQLException, ClassNotFoundException {
        super();
    }

    @Override
    public Driver findById(int id) throws SQLException, ClassNotFoundException {
        String statement = String.format("Select * From Vw_Driver where id = %d", id);
        ResultSet resultSet = executeQuery(statement);
        if (resultSet.next())
            return fillData(resultSet);
        return null;
    }

    @Override
    public Driver[] findAll() throws SQLException, ClassNotFoundException {
        String statement = String.format("Select * From Vw_Driver");
        ResultSet resultSet = executeQuery(statement);
        int rowCount = findRowCount(resultSet);
        Driver[] drivers = new Driver[rowCount];
        for (int i = 0; i < rowCount; i++) {
            if(resultSet.next())
                drivers[i] = fillData(resultSet);
        }
        return drivers;
    }

    @Override
    public int create(Object object) throws SQLException {
        try {
            Driver driver = (Driver) object;
            String statement = String.format("Insert Into Tb_Driver(user_name, first_name, last_name, nation_code, vehicle_id) " +
                    "values('%s', '%s', '%s', '%s', %d)", driver.getUserName(), driver.getFirstName(), driver.getLastName(), driver.getNationCode(), driver.getVehicle().getId());
            return executeUpdate(statement);
        } catch (ClassCastException ex) {
            System.out.println("The Object Is Not Driver.");
        }
        return 0;
    }

    public Driver findByUserName(String userName) throws SQLException, ClassNotFoundException {
        String statement = String.format("Select * From Vw_Driver where lower(user_name) = '%s'", userName.toLowerCase());
        ResultSet resultSet = executeQuery(statement);
        if (resultSet.next())
            return fillData(resultSet);
        return null;
    }

    @Override
    protected Driver fillData(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        Driver driver = new Driver();
        driver.setId(resultSet.getInt("id"));
        driver.setUserName(resultSet.getString("user_name"));
        driver.setFirstName(resultSet.getString("first_name"));
        driver.setLastName(resultSet.getString("last_name"));
        driver.setNationCode(resultSet.getString("nation_code"));
        Vehicle vehicle = (new VehicleDao()).fillData(resultSet);
        driver.setVehicle(vehicle);
        return driver;
    }
}
