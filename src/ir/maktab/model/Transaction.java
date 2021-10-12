package ir.maktab.model;

import ir.maktab.enums.ObjectType;
import ir.maktab.enums.TransactionType;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction extends General{
    private BigDecimal amount;
    private Date transDate;
    private TransactionType type;
    private int objectId; //Passenger or Driver ID
    private ObjectType objectType;

    public Transaction(BigDecimal amount, TransactionType type, int objectId, ObjectType objectType) {
        this.amount = amount;
        this.type = type;
        this.objectId = objectId;
        this.objectType = objectType;
    }

    public Transaction() {

    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", transDate=" + transDate +
                ", type=" + type +
                ", objectId=" + objectId +
                ", objectType=" + objectType +
                '}';
    }

    public BigDecimal getAmount() {
        return amount.setScale(0, BigDecimal.ROUND_UP);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }
}
