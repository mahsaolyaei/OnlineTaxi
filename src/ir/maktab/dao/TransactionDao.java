package ir.maktab.dao;

import ir.maktab.enums.ObjectType;
import ir.maktab.enums.TransactionType;
import ir.maktab.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao extends GeneralDao {

    public TransactionDao() throws SQLException, ClassNotFoundException {
        super();
    }

    public List findByObjectId(int objectId) throws SQLException, ClassNotFoundException {
        String statement = String.format("Select * From Tb_Transaction where object_id = %d", objectId);
        ResultSet resultSet = executeQuery(statement);
        List<Transaction> transactionList = new ArrayList<>();
        while(resultSet.next()) {
            transactionList.add(fillData(resultSet));
        }
        return transactionList;
    }

    @Override
    public int create(Object object) throws SQLException {
        try {
            Transaction transaction = (Transaction) object;
            String statement = String.format("Insert Into Tb_Transaction(object_id, object_type, amount, trans_date) " +
                    "values(%d, '%s', %f, sysdate())", transaction.getObjectId(), transaction.getObjectType(), transaction.getAmount());
            return executeCreate(statement);
        } catch (ClassCastException ex) {
            System.out.println("The Object Is Not Transaction.");
        }
        return 0;
    }

    @Override
    protected Transaction fillData(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        Transaction transaction = new Transaction();
        transaction.setId(resultSet.getInt("id"));
        transaction.setTransDate(resultSet.getDate("trans_date"));
        transaction.setAmount(resultSet.getBigDecimal("amount"));
        transaction.setType(TransactionType.valueOf(resultSet.getString("trans_type")));
        transaction.setObjectId(resultSet.getInt("object_id"));
        transaction.setObjectType(ObjectType.valueOf(resultSet.getString("object_type")));
        return transaction;
    }
}
