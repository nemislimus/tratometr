package com.nemislimus.tratometr.expenses.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper (context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Создание таб. EXPENSES
        db.execSQL(
            "CREATE TABLE EXPENSES ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "DATE INTEGER NOT NULL,"          // Дата в милисекундах
                    + "AMOUNT INTEGER NOT NULL,"        // Сумма в копейках
                    + "CATEGORY TEXT NOT NULL,"         // Категория
                    + "NOTE TEXT);"                     // Примечание
        )

        // Создание таб. CATEGORIES
        db.execSQL(
            "CREATE TABLE CATEGORIES ("
                    + "CATEGORY_NAME TEXT PRIMARY KEY," // Название категории - первичный ключ
                    + "ICON_RES_ID INTEGER NOT NULL);"           // id иконки
        )
    }

    // Обновление/миграция БД
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) { }

    companion object {
        private const val DB_NAME = "DBExpenses"
        private const val DB_VERSION = 1
    }
}