package com.nemislimus.tratometr.common

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.nemislimus.tratometr.R
import com.nemislimus.tratometr.databinding.ActivityMainBinding
import com.nemislimus.tratometr.expenses.data.database.DBHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var expensesBackPressedCallback: OnBackPressedCallback
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: SQLiteDatabase
    private var isBackPressedOnce = false // Флаг для отслеживания нажатия Back

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        expensesBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isBackPressedOnce) {
                    finish()                            // Завершаем активность
                } else {
                    isBackPressedOnce = true            // Устанавливаем флаг
                    Toast.makeText(this@MainActivity, R.string.text_exit, Toast.LENGTH_SHORT).show()
                    // Запускаем корутину для сброса флага через 2 секунды
                    lifecycleScope.launch {
                        delay(2000)
                        isBackPressedOnce = false       // Сбрасываем флаг
                    }
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, expensesBackPressedCallback)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainFragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            expensesBackPressedCallback.isEnabled = destination.id == R.id.expensesFragment
        }








        // Создание БД
        val dbHelper = DBHelper(this)
        database = dbHelper.writableDatabase

    }

    override fun onDestroy() {
        database.close()
        super.onDestroy()
    }
}
