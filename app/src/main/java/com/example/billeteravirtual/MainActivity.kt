package com.example.billeteravirtual

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var transactionDao: DollarTransactionDao
    private lateinit var historyTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar la base de datos Room
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "dollar_database"
        ).build()

        transactionDao = db.dollarTransactionDao()

        // Vincular vistas
        val buyButton: Button = findViewById(R.id.buyButton)
        val sellButton: Button = findViewById(R.id.sellButton)
        val amountEditText: EditText = findViewById(R.id.amountEditText)
        historyTextView = findViewById(R.id.historyTextView)

        // Configurar botones
        buyButton.setOnClickListener {
            val amountStr = amountEditText.text.toString()
            if (amountStr.isNotEmpty()) {
                handleTransaction("Compra", amountStr.toDouble())
                amountEditText.text.clear()
            }
        }

        sellButton.setOnClickListener {
            val amountStr = amountEditText.text.toString()
            if (amountStr.isNotEmpty()) {
                handleTransaction("Venta", amountStr.toDouble())
                amountEditText.text.clear()
            }
        }

        // Cargar historial al inicio
        loadHistory()
    }

    private fun handleTransaction(type: String, amount: Double) {
        val transaction = DollarTransaction(
            type,
            amount,
            System.currentTimeMillis()
        )
        CoroutineScope(Dispatchers.IO).launch {
            transactionDao.insert(transaction)
            loadHistory() // Actualizar el historial después de insertar
        }
    }

    private fun loadHistory() {
        CoroutineScope(Dispatchers.IO).launch {
            val transactions = transactionDao.getAllTransactions()
            val historyText = StringBuilder("Historial de transacciones:\n\n")
            for (t in transactions) {
                historyText.append("${t.type}: $${t.amount}\n")
            }
            
            // Actualizar la vista en el hilo principal
            withContext(Dispatchers.Main) {
                historyTextView.text = historyText.toString()
            }
        }
    }
}