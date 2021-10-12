package ir.maktab.dao;

import ir.maktab.enums.PaymentType;
import ir.maktab.enums.TripStatus;
import ir.maktab.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TripDao extends GeneralDao{

    public TripDao() throws SQLException, ClassNotFoundException {
        super();
    }

    @Override
    public List findAll() throws SQLException, ClassNotFoundException {
        String statement = "Select * From Vw_Trip";
        return findByStatement(statement);
    }

    public List findOngoingTrips() throws SQLException, ClassNotFoundException {
        String statement = "Select * From Vw_OnGoingTrip";
        return findByStatement(statement);
    }

    public List findByStatement(String statement) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = executeQuery(statement);
        List<Trip> tripList = new ArrayList<>();
        while(resultSet.next()) {
            tripList.add(fillData(resultSet));
        }
        return tripList;
    }

    @Override
    public int create(Object object) throws SQLException {
        try {
            Trip trip = (Trip) object;
            String statement = String.format("Insert Into Tb_Trip(passenger_id, driver_id, " +
                    "trip_status, trip_amount, payment_type, origin_long, origin_lat, dest_long, dest_lat) " +
                    "values(%d, %d, '%s', %f, '%s', %d, %d, %d, %d)", trip.getPassenger().getId(),
                    trip.getDriver().getId(), trip.getStatus(), trip.getAmount(), trip.getPaymentType(),
                    trip.getOrigin().getLongitude(), trip.getOrigin().getLatitude(),
                    trip.getDestination().getLongitude(), trip.getDestination().getLatitude());
            return executeCreate(statement);
        } catch (ClassCastException ex) {
            System.out.println("The Object Is Not Driver.");
        }
        return 0;
    }

    public int confirmPayment(Trip trip) throws SQLException {
        String statement = String.format("Update Tb_Trip Set payment_confirmed = 1 " +
                        "Where Id = %d", trip.getId());
        return executeUpdate(statement);
    }

    public int changeStatus(Trip trip) throws SQLException {
        String statement = String.format("Update Tb_Trip Set trip_status = '%s' " +
                "Where Id = %d", trip.getStatus(), trip.getId());
        return executeUpdate(statement);
    }

    public Trip findOnGoingTripByDriver(Driver driver) throws SQLException, ClassNotFoundException {
        String statement = String.format("Select * From Vw_OnGoingTrip where driver_id = %d", driver.getId());
        ResultSet resultSet = executeQuery(statement);
        if (resultSet.next())
            return fillData(resultSet);
        return null;
    }

    @Override
    protected Trip fillData(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        Trip trip = new Trip();
        trip.setId(resultSet.getInt("id"));
        Passenger passenger = (new PassengerDao()).fillData(resultSet, true);
        Driver driver = (new DriverDao()).fillData(resultSet, true);
        Location origin = new Location(resultSet.getInt("origin_long"), resultSet.getInt("origin_lat"));
        Location destination = new Location(resultSet.getInt("dest_long"), resultSet.getInt("dest_lat"));
        trip.setPassenger(passenger);
        trip.setDriver(driver);
        trip.setOrigin(origin);
        trip.setDestination(destination);
        trip.setAmount(resultSet.getBigDecimal("trip_amount"));
        trip.setStatus(TripStatus.valueOf(resultSet.getString("trip_status")));
        trip.setPaymentType(PaymentType.valueOf(resultSet.getString("payment_type")));
        int type = resultSet.getInt("payment_confirmed");
        trip.setPaymentConfirmed(type > 0 ? true: false);
        return trip;
    }
}
