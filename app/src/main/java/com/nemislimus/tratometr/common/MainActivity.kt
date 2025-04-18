package com.nemislimus.tratometr.common

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nemislimus.tratometr.databinding.ActivityMainBinding
import com.nemislimus.tratometr.expenses.data.database.DBHelper

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Создание БД
        val dbHelper = DBHelper(this)
        database = dbHelper.writableDatabase

    }

    override fun onDestroy() {
        database.close()
        super.onDestroy()
    }
}
