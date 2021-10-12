package ir.maktab.dao;

import ir.maktab.model.Vehicle;
import ir.maktab.enums.VehicleType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleDao extends GeneralDao {

    public VehicleDao() throws SQLException, ClassNotFoundException {
        super();
    }

    @Override
    public int create(Object object) throws SQLException {
        try {
            Vehicle vehicle = (Vehicle) object;
            String statement = String.format("Insert Into Tb_Vehicle(vehicle_number, model, color, vehicle_type) " +
                    "values('%s', '%s', '%s', '%s')", vehicle.getNumber(), vehicle.getModel(), vehicle.getColor(), vehicle.getType());
            return executeCreate(statement);
        } catch (ClassCastException ex) {
            System.out.println("The Object Is Not Driver.");
        }
        return 0;
    }

    @Override
    public Vehicle findById(int id) throws SQLException {
        String statement = String.format("Select * From Tb_Vehicle where id = %d", id);
        ResultSet resultSet = executeQuery(statement);
        if (resultSet.next())
            return fillData(resultSet);
        return null;
    }

    @Override
    public Vehicle fillData(ResultSet resultSet) throws SQLException {
        Vehicle vehicle = new Vehicle();
        int vehicleId;
        try {
            vehicleId = resultSet.getInt("vehicle_id");
        }
        catch (SQLException ex) {
            vehicleId = resultSet.getInt("id");
        }
        vehicle.setId(vehicleId);
        vehicle.setNumber(resultSet.getString("vehicle_number"));
        vehicle.setModel(resultSet.getString("model"));
        vehicle.setColor(resultSet.getString("color"));
        vehicle.setType(VehicleType.valueOf(resultSet.getString("vehicle_type")));
        return vehicle;
    }
}
