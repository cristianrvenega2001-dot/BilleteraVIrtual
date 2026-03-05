package com.example.billeteravirtual;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = { DollarTransaction.class }, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DollarTransactionDao dollarTransactionDao();
}
