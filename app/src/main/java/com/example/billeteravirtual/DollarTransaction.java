package com.example.billeteravirtual;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "dollar_transactions")
public class DollarTransaction {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String type;
    public double amount;
    public long timestamp;

    public DollarTransaction(String type, double amount, long timestamp) {
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }
}
