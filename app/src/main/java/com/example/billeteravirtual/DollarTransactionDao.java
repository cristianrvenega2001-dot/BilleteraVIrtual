package com.example.billeteravirtual;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DollarTransactionDao {
    @Insert
    void insert(DollarTransaction transaction);

    @Query("SELECT * FROM dollar_transactions ORDER BY timestamp DESC")
    List<DollarTransaction> getAllTransactions();
}
