package ir.maktab.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GeneralDao {
    protected Connection connection;

    public GeneralDao() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlineTaxi", "root", "1234");
    }

    public ResultSet executeQuery(String query) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet resultSet = statement.executeQuery(query);
        return resultSet;
    }

    public int executeUpdate(String query) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        return statement.executeUpdate(query);
    }

    public int executeCreate(String query) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        int result = statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
        if (result == 0)
            return result;
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        }
        else {
            System.out.println("no ID obtained.");
            return 0;
        }
    }

    public int findRowCount(ResultSet resultSet) throws SQLException {
        int rowCount = 0;
        if (resultSet.last()) {
            rowCount = resultSet.getRow();
            resultSet.beforeFirst();
        }
        return rowCount;
    }

    public int create(Object object) throws SQLException {
        return 0;
    }

    public Object findById(int id) throws SQLException, ClassNotFoundException {
        return null;
    }

    public List findAll() throws SQLException, ClassNotFoundException {
        return new ArrayList<>();
    }

    protected Object fillData(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        return null;
    }

}
