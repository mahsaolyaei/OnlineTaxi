package ir.maktab.dao;

import ir.maktab.enums.UserStatus;
import ir.maktab.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public List findAll() throws SQLException, ClassNotFoundException {
        String statement = "Select * From Vw_Driver";
        return findByStatement(statement);
    }

    public List findAllWaiting() throws SQLException, ClassNotFoundException {
        String statement = String.format("Select * From Vw_Driver Where driver_status = '%s'", UserStatus.WAITING);
        return findByStatement(statement);
    }

    public List findByStatement(String statement) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = executeQuery(statement);
        List<Driver> driverList = new ArrayList<>();
        while(resultSet.next()) {
            driverList.add(fillData(resultSet));
        }
        return driverList;
    }

    @Override
    public int create(Object object) throws SQLException {
        try {
            Driver driver = (Driver) object;
            String statement = String.format("Insert Into Tb_Driver(person_id, vehicle_id, driver_user_name, driver_balance, " +
                    "driver_long, driver_lat, driver_status) values(%d, %d, '%s', %f, %d, %d, '%s')", driver.getPerson().getId(),
                    driver.getVehicle().getId(), driver.getUserName(), driver.getBalance(), driver.getLocation().getLongitude(),
                    driver.getLocation().getLatitude(), driver.getStatus());
            return executeCreate(statement);
        } catch (ClassCastException ex) {
            System.out.println("The Object Is Not Driver.");
        }
        return 0;
    }

    public Driver findByUserName(String userName) throws SQLException, ClassNotFoundException {
        String statement = String.format("Select * From Vw_Driver where lower(driver_user_name) = '%s'", userName.toLowerCase());
        ResultSet resultSet = executeQuery(statement);
        if (resultSet.next())
            return fillData(resultSet);
        return null;
    }

    public int changeTripFinishingFields(Driver driver) throws SQLException {
        String statement = String.format("Update Tb_Driver Set driver_status = '%s', driver_long = %d, driver_lat = %d, driver_balance = %f" +
                " Where Id = %d", driver.getStatus(), driver.getLocation().getLongitude(),
                driver.getLocation().getLatitude(), driver.getBalance(), driver.getId());
        return executeUpdate(statement);
    }

    public int changeStatus(Driver driver) throws SQLException {
        String statement = String.format("Update Tb_Driver Set driver_status = '%s'" +
                        " Where Id = %d", driver.getStatus(), driver.getId());
        return executeUpdate(statement);
    }

    @Override
    protected Driver fillData(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        return fillData(resultSet, false);
    }

    public Driver fillData(ResultSet resultSet, boolean hasSpecialPerson) throws SQLException, ClassNotFoundException {
        Driver driver = new Driver();
        int driverId;
        try {
            driverId = resultSet.getInt("driver_id");
        }
        catch (SQLException ex) {
            driverId = resultSet.getInt("id");
        }
        driver.setId(driverId);
        Person person = (new PersonDao()).fillData(resultSet, hasSpecialPerson? "driver_": "");
        Vehicle vehicle = (new VehicleDao()).fillData(resultSet);
        Location location = new Location(resultSet.getInt("driver_long"), resultSet.getInt("driver_lat"));
        driver.setPerson(person);
        driver.setVehicle(vehicle);
        driver.setLocation(location);
        driver.setUserName(resultSet.getString("driver_user_name"));
        driver.setBalance(resultSet.getBigDecimal("driver_balance"));
        driver.setStatus(UserStatus.valueOf(resultSet.getString("driver_status")));
        return driver;
    }
}
